package com.android.sdklib.internal.build;

/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.DigestOutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.spongycastle.asn1.cms.ContentInfo;
import org.spongycastle.cert.jcajce.JcaCertStore;
import org.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import org.spongycastle.cms.CMSException;
import org.spongycastle.cms.CMSProcessableByteArray;
import org.spongycastle.cms.CMSSignedData;
import org.spongycastle.cms.CMSSignedDataGenerator;
import org.spongycastle.cms.CMSTypedData;
import org.spongycastle.cms.SignerInfoGenerator;
import org.spongycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.DigestCalculatorProvider;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.spongycastle.util.Store;
import org.spongycastle.util.encoders.Base64;

import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter.ZipAbortException;

/**
 * A Jar file builder with signature support.
 */
public class SignedJarBuilder {
    private static final String DIGEST_ALGORITHM = "SHA1";
    private static final String DIGEST_ATTR = "SHA1-Digest";
    private static final String DIGEST_MANIFEST_ATTR = "SHA1-Digest-Manifest";

    /** Write to another stream and also feed it to the Signature object. */
    private static class SignatureOutputStream extends OutputStream {
		private OutputStream first;
        private OutputStream second;
        private int mCount = 0;

        public SignatureOutputStream(OutputStream first, OutputStream second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public void write(int b) throws IOException {
			first.write(b);
            second.write(b);
            mCount++;
        }

		@Override
		public void write(byte[] b) throws IOException {
			first.write(b);
			second.write(b);
		}

		@Override
        public void write(byte[] b, int off, int len) throws IOException {
			first.write(b, off, len);
            second.write(b, off, len);
            mCount += len;
        }

		@Override
		public void flush() throws IOException {
			first.flush();
			second.flush();
		}

		@Override
		public void close() throws IOException {
			first.close();
			second.close();
		}

		public int size() {
            return mCount;
        }
    }

	private static class AlignedZipOutputStream extends ZipOutputStream {

		public static AlignedZipOutputStream create(OutputStream out, int alignment) throws IOException {
			return new AlignedZipOutputStream(new PositionOutputStream(out), alignment);
		}

		private final PositionOutputStream out;
		private final int alignment;

		private AlignedZipOutputStream(PositionOutputStream out, int alignment) throws IOException {
			super(out);
			this.out = out;
			this.alignment = alignment;
		}

		@Override
		public void putNextEntry(ZipEntry ze) throws IOException {
			// Zip-align entries: Add padding to extra data for STORED entries
			if (ze.getMethod() != ZipEntry.DEFLATED) {
				long dataOffset = out.pos + getHeaderSize(ze);
				int padding = (int) ((alignment - (dataOffset % alignment)) % alignment);
				if (padding > 0) {
					byte[] extra = ze.getExtra();
					if (extra == null) {
						ze.setExtra(new byte[padding]);
					} else {
						byte[] bytes = Arrays.copyOf(extra, extra.length + padding);
						ze.setExtra(bytes);
					}
				}
			}
			super.putNextEntry(ze);
		}

		private long getHeaderSize(ZipEntry ze) {
			int len = 30 + getNameLength(ze.getName());

			byte[] extra = ze.getExtra();
			if (extra != null) {
				len += extra.length;
			}
			return len;
		}

		private int getNameLength(String s) {
			char[] chars = s.toCharArray();

			int count = 0;
			for (char ch : chars) {
				if (ch <= 0x7f) {
					count++;
				} else if (ch <= 0x7ff) {
					count += 2;
				} else {
					count += 3;
				}
			}
			return count;
		}
	}

	/**
	 * A output stream wrapper that knows its current position.
	 */
	private static class PositionOutputStream extends OutputStream {
		private final OutputStream out;
		private long pos;

		public PositionOutputStream(OutputStream out) {
			this.out = out;
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
			++pos;
		}

		@Override
		public void write(byte[] b) throws IOException {
			out.write(b);
			pos += b.length;
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			pos += len;
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void close() throws IOException {
			out.close();
		}
	}

    private ZipOutputStream mOutputJar;
    private PrivateKey mKey;
    private X509Certificate mCertificate;
    private Manifest mManifest;
    private MessageDigest mMessageDigest;

    private byte[] mBuffer = new byte[4096];
	private boolean canceled;

    /**
     * Classes which implement this interface provides a method to check whether a file should
     * be added to a Jar file.
     */
    public interface IZipEntryFilter {

        /**
         * An exception thrown during packaging of a zip file into APK file.
         * This is typically thrown by implementations of
         * {@link IZipEntryFilter#checkEntry(String)}.
         */
        public static class ZipAbortException extends Exception {
            private static final long serialVersionUID = 1L;

            public ZipAbortException() {
                super();
            }

            public ZipAbortException(String format, Object... args) {
                super(String.format(format, args));
            }

            public ZipAbortException(Throwable cause, String format, Object... args) {
                super(String.format(format, args), cause);
            }

            public ZipAbortException(Throwable cause) {
                super(cause);
            }
        }


        /**
         * Checks a file for inclusion in a Jar archive.
         * @param archivePath the archive file path of the entry
         * @return <code>true</code> if the file should be included.
         * @throws ZipAbortException if writing the file should be aborted.
         */
        public boolean checkEntry(String archivePath) throws ZipAbortException;
    }

    /**
     * Creates a {@link SignedJarBuilder} with a given output stream, and signing information.
     * <p/>If either <code>key</code> or <code>certificate</code> is <code>null</code> then
     * the archive will not be signed.
     * @param out the {@link OutputStream} where to write the Jar archive.
     * @param key the {@link PrivateKey} used to sign the archive, or <code>null</code>.
     * @param certificate the {@link X509Certificate} used to sign the archive, or
     * <code>null</code>.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public SignedJarBuilder(OutputStream out, PrivateKey key, X509Certificate certificate)
            throws IOException, NoSuchAlgorithmException {
		mOutputJar = AlignedZipOutputStream.create(out, 4);
        mOutputJar.setLevel(9);
        mKey = key;
        mCertificate = certificate;

        if (mKey != null && mCertificate != null) {
            mManifest = new Manifest();
            Attributes main = mManifest.getMainAttributes();
            main.putValue("Manifest-Version", "1.0");
            main.putValue("Created-By", "1.0 (Android)");

            mMessageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        }
    }

    /**
     * Writes a new {@link File} into the archive.
     * @param inputFile the {@link File} to write.
     * @param jarPath the filepath inside the archive.
     * @throws IOException
     */
    public void writeFile(File inputFile, String jarPath) throws IOException {
        // Get an input stream on the file.
        FileInputStream fis = new FileInputStream(inputFile);
        try {

            // create the zip entry
            JarEntry entry = new JarEntry(jarPath);
            entry.setTime(inputFile.lastModified());

            writeEntry(fis, entry);
        } finally {
            // close the file stream used to read the file
            fis.close();
        }
    }

    public void writeStream(InputStream input, String jarPath) throws IOException {
        // create the zip entry
        JarEntry entry = new JarEntry(jarPath);
        entry.setTime(System.currentTimeMillis());

        writeEntry(input, entry);
    }

    /**
     * Copies the content of a Jar/Zip archive into the receiver archive.
     * <p/>An optional {@link IZipEntryFilter} allows to selectively choose which files
     * to copy over.
     * @param input the {@link InputStream} for the Jar/Zip to copy.
     * @param filter the filter or <code>null</code>
     * @throws IOException
     * @throws ZipAbortException if the {@link IZipEntryFilter} filter indicated that the write
     *                           must be aborted.
     * @throws InterruptedException 
     */
    public void writeZip(InputStream input, IZipEntryFilter filter)
            throws IOException, ZipAbortException, InterruptedException {
        ZipInputStream zis = new ZipInputStream(input);

        try {
            // loop on the entries of the intermediary package and put them in the final package.
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
            	checkCancelled();
                String name = entry.getName();

                // do not take directories or anything inside a potential META-INF folder.
                if (entry.isDirectory() || name.startsWith("META-INF/")) {
                    continue;
                }

                // if we have a filter, we check the entry against it
                if (filter != null && !filter.checkEntry(name)) {
                    continue;
                }

                // Preserve the STORED method of the input entry.
                JarEntry newEntry;
                if (entry.getMethod() == JarEntry.STORED) {
                    newEntry = new JarEntry(entry);
                } else {
                    // Create a new entry so that the compressed len is recomputed.
                    newEntry = new JarEntry(name);
                }

                writeEntry(zis, newEntry);

                zis.closeEntry();
            }
        } finally {
            zis.close();
        }
    }

    /**
     * Copies the content of a Jar/Zip archive into the receiver archive.
     * <p/>An optional {@link IZipEntryFilter} allows to selectively choose which files
     * to copy over.
     * @param zipFile the {@link ZipFile} for the Jar/Zip to copy.
     * @param filter the filter or <code>null</code>
     * @throws IOException
     * @throws ZipAbortException if the {@link IZipEntryFilter} filter indicated that the write
     *                           must be aborted.
     * @throws InterruptedException 
     */
    public void writeZip(ZipFile zipFile, IZipEntryFilter filter)
            throws IOException, ZipAbortException, InterruptedException {
        // loop on the entries of the intermediary package and put them in the final package.
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
        	checkCancelled();
            String name = entry.getName();

            // do not take directories or anything inside a potential META-INF folder.
            if (entry.isDirectory() || name.startsWith("META-INF/")) {
                continue;
            }

            // if we have a filter, we check the entry against it
            if (filter != null && !filter.checkEntry(name)) {
                continue;
            }

            // Preserve the STORED method of the input entry.
            JarEntry newEntry;
            if (entry.getMethod() == JarEntry.STORED) {
                newEntry = new JarEntry(entry);
            } else {
                // Create a new entry so that the compressed len is recomputed.
                newEntry = new JarEntry(name);
            }

            writeEntry(zipFile.getInputStream(entry), newEntry);
        }
    }

    /**
     * Closes the Jar archive by creating the manifest, and signing the archive.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void close() throws IOException, GeneralSecurityException {
        if (mManifest != null) {
            // write the manifest to the jar file
            mOutputJar.putNextEntry(new JarEntry(JarFile.MANIFEST_NAME));
            mManifest.write(mOutputJar);

            // CERT.SF
            ByteArrayOutputStream plain = new ByteArrayOutputStream();
            mOutputJar.putNextEntry(new JarEntry("META-INF/CERT.SF"));
            writeSignatureFile(new SignatureOutputStream(mOutputJar, plain));

            // CERT.*
            mOutputJar.putNextEntry(new JarEntry("META-INF/CERT." + mKey.getAlgorithm()));
            writeSignatureBlock(plain.toByteArray(), mCertificate, mKey);
        }

        mOutputJar.close();
    }

	private void checkCancelled() throws InterruptedException {
		if(canceled) {
			throw new InterruptedException();
		}
	}

    /**
     * Adds an entry to the output jar, and write its content from the {@link InputStream}
     * @param input The input stream from where to write the entry content.
     * @param entry the entry to write in the jar.
     * @throws IOException
     */
    private void writeEntry(InputStream input, JarEntry entry) throws IOException {
        // add the entry to the jar archive
        mOutputJar.putNextEntry(entry);

        // read the content of the entry from the input stream, and write it into the archive.
        int count;
        while ((count = input.read(mBuffer)) != -1) {
            mOutputJar.write(mBuffer, 0, count);

            // update the digest
            if (mMessageDigest != null) {
                mMessageDigest.update(mBuffer, 0, count);
            }
        }

        // close the entry for this file
        mOutputJar.closeEntry();

        if (mManifest != null) {
            // update the manifest for this entry.
            Attributes attr = mManifest.getAttributes(entry.getName());
            if (attr == null) {
                attr = new Attributes();
                mManifest.getEntries().put(entry.getName(), attr);
            }
            attr.putValue(DIGEST_ATTR, new String(Base64.encode(mMessageDigest.digest())));
        }
    }

    /** Writes a .SF file with a digest to the manifest. */
    private void writeSignatureFile(final SignatureOutputStream out)
            throws IOException, GeneralSecurityException {
    	
    	Manifest sf = new Manifest();

    	// Ok, this is messed up. In the Android version of Manifest.java, the
        // main attributes are only written if the Manifest-Version attribute is set
        MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
        PrintStream print = new PrintStream(
                new DigestOutputStream(new ByteArrayOutputStream(), md),
                true, "UTF-8");

        // Digest of the entire manifest
        mManifest.write(print);
        print.flush();
        final String digest = new String(Base64.encode(md.digest()));

    	//Attributes main = sf.getMainAttributes();
        //main.putValue("Signature-Version", "1.0");
        //main.putValue("Created-By", "1.0 (Android)");
        //main.putValue(DIGEST_MANIFEST_ATTR, digest);
		out.write("Signature-Version: 1.0\r\n".getBytes());
		out.write("Created-By: 1.0 (Android)\r\n".getBytes());
		out.write((DIGEST_MANIFEST_ATTR + ": " + digest + "\r\n").getBytes());

        
        Map<String, Attributes> entries = mManifest.getEntries();
        for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
            // Digest of the manifest stanza for this entry.
            print.print("Name: " + entry.getKey() + "\r\n");
            for (Map.Entry<Object, Object> att : entry.getValue().entrySet()) {
                print.print(att.getKey() + ": " + att.getValue() + "\r\n");
            }
            print.print("\r\n");
            print.flush();

            Attributes sfAttr = new Attributes();
            sfAttr.putValue(DIGEST_ATTR, new String(Base64.encode(md.digest())));
            sf.getEntries().put(entry.getKey(), sfAttr);
        }

        sf.write(out);

        // A bug in the java.util.jar implementation of Android platforms
        // up to version 1.6 will cause a spurious IOException to be thrown
        // if the length of the signature file is a multiple of 1024 bytes.
        // As a workaround, add an extra CRLF in this case.
        if ((out.size() % 1024) == 0) {
            out.write('\r');
            out.write('\n');
        }
    }

    /** Write the certificate file with a digital signature. */
	private void writeSignatureBlock(byte[] bs, X509Certificate publicKey, PrivateKey privateKey) throws IOException {
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			String algorithm = DIGEST_ALGORITHM + "with" + privateKey.getAlgorithm();
			
			// Create signer information from public certificate
			DigestCalculatorProvider calculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider("SC").build();
			ContentSigner signer = new JcaContentSignerBuilder(algorithm).setProvider("SC").build(privateKey);
			JcaX509CertificateHolder publicCert = new JcaX509CertificateHolder(publicKey);
			
			SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(calculatorProvider).setDirectSignature(true).build(signer, publicCert);

			// Load certificates
			List<X509Certificate> certList = new ArrayList<X509Certificate>();
			certList.add(publicKey);
			Store certStore = new JcaCertStore(certList);
			
			// Create PKCS7/CMS generator
			CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
			gen.addSignerInfoGenerator(signerInfoGenerator);
			gen.addCertificates(certStore);
			
			// Create PKCS7
			CMSTypedData data = new CMSProcessableByteArray(bs);
			CMSSignedData signedData = gen.generate(data, false);

			// Transform BER encoding to DER encoding
			ContentInfo contentInfo = signedData.toASN1Structure(); 
			byte[] enveloped = contentInfo.getEncoded("DER");
			
			// Write PKCS7 to JAR file
			mOutputJar.write(enveloped);
		} catch (OperatorCreationException e) {
			throw new IOException(e);
		} catch (CertificateEncodingException e) {
			throw new IOException(e);
		} catch (CMSException e) {
			throw new IOException(e);
		}
	}
	
	public void cancel() {
		canceled = true;
	}
}
