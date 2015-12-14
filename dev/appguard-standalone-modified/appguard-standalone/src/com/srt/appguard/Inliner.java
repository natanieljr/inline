package com.srt.appguard;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.build.SignedJarBuilder;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter;
import com.android.sdklib.internal.build.SignedJarBuilder.IZipEntryFilter.ZipAbortException;
import com.srt.appguard.axml.AndroidManifest;
import com.srt.appguard.axml.chunks.Element;

public class Inliner {

	private static final String ASSETS_APP_DEX = "assets/appguard/app.dex";
	private static final String ASSETS_APP_INFO = "assets/appguard/appguard.properties";

	private final File loaderDexFile;
	private final String loaderApplicationClass;

	public Inliner(File loaderDexFile, String loaderApplicationClass) throws IOException {
		this.loaderDexFile = loaderDexFile;
		this.loaderApplicationClass = loaderApplicationClass;
	}

	/**
	 *
	 * @param privateKey
	 *            The private key to sign the application package.
	 * @param certificate
	 *            The certificate used for signing.
	 * @param apkOut
	 *            Path to the final apk file.
	 * @param apkFile
	 *            The original application apk|dex file.
	 * @throws InlineException
	 *             If the inlining process fails.
	 */
	public void inline(PrivateKey privateKey, X509Certificate certificate, OutputStream apkOut, File apkFile, Properties appProps) throws InlineException {
		ZipFile apkZipFile = null;
		try {
			apkZipFile = new ZipFile(apkFile);

			// Init the jarbuilder
			final SignedJarBuilder builder = new SignedJarBuilder(apkOut, privateKey, certificate);

			// Copy apkIn to apkOut but skip classes.dex and AndroidManifest.xml
			builder.writeZip(apkZipFile, new IZipEntryFilter() {
				@Override
				public boolean checkEntry(String name) throws ZipAbortException {
					if (SdkConstants.FN_APK_CLASSES_DEX.equals(name)) {
						return false;
					} else if (SdkConstants.FN_ANDROID_MANIFEST_XML.equals(name)) {
						return false;
					} else {
						return true;
					}
				}
			});

			// Write original classes.dex as app.dex to apk
			ZipEntry classesDex = apkZipFile.getEntry(SdkConstants.FN_APK_CLASSES_DEX);
			builder.writeStream(apkZipFile.getInputStream(classesDex), ASSETS_APP_DEX);

			// Write classes.dex to apk
			final InputStream in = new BufferedInputStream(new FileInputStream(loaderDexFile));
			builder.writeStream(in, SdkConstants.FN_APK_CLASSES_DEX);
			in.close();

			// Parse AndroidManifest.xml
			ZipEntry manifestEntry = apkZipFile.getEntry(SdkConstants.FN_ANDROID_MANIFEST_XML);
			InputStream manifestStream = apkZipFile.getInputStream(manifestEntry);
			AndroidManifest manifest = new AndroidManifest(manifestStream);
			manifestStream.close();

			// Modify AndroidManifest.xml
			String applicationName = updateApplicationClass(manifest);
			addInternetPermission(manifest);

			// Write modified AndroidManifest.xml to apk
			ByteArrayOutputStream manifestOut = new ByteArrayOutputStream();
			manifest.write(manifestOut);
			builder.writeStream(new ByteArrayInputStream(manifestOut.toByteArray()), SdkConstants.FN_ANDROID_MANIFEST_XML);

			// Write app.info
			if (applicationName != null) {
				appProps.setProperty("appApplicationClass", applicationName);
			}
			ByteArrayOutputStream appPropsOut = new ByteArrayOutputStream();
			appProps.storeToXML(appPropsOut, null);
			builder.writeStream(new ByteArrayInputStream(appPropsOut.toByteArray()), ASSETS_APP_INFO);

			// Sign & close
			builder.close();
		} catch (NoSuchAlgorithmException e) {
			throw new InlineException(e);
		} catch (FileNotFoundException e) {
			throw new InlineException(e);
		} catch (ZipException e) {
			throw new InlineException(e);
		} catch (ZipAbortException e) {
			throw new InlineException(e);
		} catch (IOException e) {
			throw new InlineException(e);
		} catch (InterruptedException e) {
			throw new InlineException(e);
		} catch (GeneralSecurityException e) {
			throw new InlineException(e);
		} finally {
			// Close opened apk file
			if (apkZipFile != null) {
				try {
					apkZipFile.close();
				} catch (IOException e) {
					// Ignore failed close()
					e.printStackTrace();
				}
			}
		}
	}

	// KJA probably somewhere here I will have add adding the "Internet" permission.
	/**
	 * Extract the application class name from the AndroidManifest.xml. In case
	 * no application class is defined, the {@link #loaderApplicationClass}
	 * class is set as the application class.
	 *
	 * @param manifest
	 *            The AndroidManifest file.
	 * @return The fully qualified name of the defined application class or
	 *         <code>null</code> if no application class was defined.
	 */
	private String updateApplicationClass(AndroidManifest manifest) {
		Element manifestElement = manifest.getElement("manifest");
		Element applicationElement = manifest.getElement("application");

		if (applicationElement == null) {
			// Create application element if it does not exist
			applicationElement = manifest.createElement(manifestElement, "application");
		}

		String applicationName = applicationElement.getAttributeValue("http://schemas.android.com/apk/res/android", "name", android.R.attr.name);
		if (applicationName != null && (applicationName.startsWith(".") || !applicationName.contains("."))) {
			// Prepend packageName if application class start with a dot or
			// if it does not contain a dot at all
			String packageName = manifestElement.getAttributeValue(null, "package");

			if (applicationName.startsWith(".")) {
				applicationName = packageName + applicationName;
			} else {
				applicationName = packageName + "." + applicationName;
			}
		}

		// Set AppGuard application class
		applicationElement.setAttribute("http://schemas.android.com/apk/res/android", "name", android.R.attr.name, loaderApplicationClass);

		return applicationName;
	}

	/**
	 * <p>
	 * Adds the INTERNET permission to the AndroidManifest.xml. If the manifest already has it it will end up having it duplicated,
	 * but this shouldn't be a problem.
	 *
	 * </p><p>
	 * This method was added to facilitate usage of BoxMate's monitor.java, which launches a TCP server and establishes TCP
	 * connection with the host machine to transfer to it strings with information about logged monitored API calls. Such TCP
	 * connection requires the INTERNET permission.
	 *
	 * </p>
	 * @since 28 July 2105, by Konrad Jamrozik
	 */
	private void addInternetPermission(AndroidManifest manifest) {
		Element manifestElement = manifest.getElement("manifest");
		Element usesPermissionElement = manifest.createElement(manifestElement, "uses-permission");
		usesPermissionElement.setAttribute("http://schemas.android.com/apk/res/android", "name", android.R.attr.name, "android.permission.INTERNET");
	}

	public static class InlineException extends Exception {
		private static final long serialVersionUID = 1L;

		public InlineException(Throwable e) {
			super(e);
		}
	}

	private static class android {
		private static class R {
			private static class attr {
				public static final int name = 0x1010003;
			}
		}
	}

	public interface InlineListener {
		public void updateProgress(int progress, int max);
	}

}
