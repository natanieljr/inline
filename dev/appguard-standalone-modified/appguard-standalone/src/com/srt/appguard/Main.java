package com.srt.appguard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import com.srt.appguard.monitor.AppGuardProperties;

public class Main {

  // Replaced by args[1].
	private static final String LOADER_FILE = "appguard-loader.dex";
	private static final String LOADER_CLASS = "com.srt.appguard.loader.MonitorLoaderApplication";

	private static final String KEYSTORE_FILE = "keystore.bks";
	private static final String KEYSTORE_PASSWORD = "android";
	private static final String KEY_ALIAS = "androiddebugkey";
	private static final String KEY_PASSWORD = "android";

	/**
	 * Usage: inliner.bat <target-apk> <monitor-apk> <monitor-class>
	 *
	 * <target-apk>  		path to the target apk file (on machine, e.g. C:\example\targetapp.apk)
	 * <monitor-apk>		path to the monitor apk file (on device, world-readable, e.g. /data/monitor/monitor.apk)
	 * <monitor-class>		name of the main Monitor class (e.g. com.example.Monitor)
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 4) {
			printUsage();
			System.out.println("No. of args: " + args.length);
      for (String arg : args)
      {
        System.out.println("arg: " + arg);
      }
      return;
		}

		OutputStream apkOut = null;
		try {
      final String targetApkName = args[0];
      File apkFile = new File(targetApkName);
			if (apkFile.isAbsolute()) {
				if (!apkFile.exists()) {
					System.err.println("Apk file " + apkFile.getCanonicalPath() + " not found.");
					return;
				}
			} else {
				apkFile = new File(getCwd(), targetApkName);
				if (!apkFile.exists()) {
					System.err.println("Apk file " + apkFile.getCanonicalPath() + " not found.");
					return;
				}
			}

			String apkOutName = apkFile.getName();
			final int dotIdx = apkOutName.lastIndexOf('.');
			apkOutName = apkOutName.substring(0, dotIdx) + "-inlined" + apkOutName.substring(dotIdx);
			final File apkOutFile = new File(apkFile.getParent(), apkOutName);
			if (!apkOutFile.getParentFile().canWrite() || (apkOutFile.exists() && !apkOutFile.canWrite())) {
				System.err.println("Cannot write inlined apk to " + apkOutFile.getCanonicalPath());
				return;
			}
			apkOut = new FileOutputStream(apkOutFile);

			final String monitorApkPath = args[2];
			final String monitorClass = args[3];
			final AppGuardProperties appProps = new AppGuardProperties(monitorApkPath, monitorClass);

			/*
			Security.addProvider(new BouncyCastleProvider());
			final KeyCertificatePair pair = loadDefaultKey();
			if (pair == null) {
				System.err.println("Failed to load signing key");
				return;
			}
			*/

      final String loaderFilePath = args[1];
			final File loaderDexFile = new File(loaderFilePath);
			final Inliner inliner = new Inliner(loaderDexFile, LOADER_CLASS);
			inliner.inline(null, null /*pair.privateKey, pair.publicKeyCert*/, apkOut, apkFile, appProps.getProperties());

			System.out.println("Inlined apk file successfully written to " + apkOutFile.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (apkOut != null) {
				try {
					apkOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void printUsage() {
		System.out.println("Usage: inliner.bat <target-apk> <monitor-apk> <monitor-class>\n");
		System.out.println("<target-apk>  		path to the target apk file (on machine, e.g. C:\\example\\targetapp.apk)");
		System.out.println("<monitor-apk>		path to the monitor apk file (on device, world-readable, e.g. /data/monitor/monitor.apk)");
		System.out.println("<monitor-class>		name of the main Monitor class (e.g. com.example.Monitor)");
	}

	private static String getCwd() {
		return System.getProperty("user.dir");
	}

	private static class KeyCertificatePair {
		private PrivateKey privateKey;
		private X509Certificate publicKeyCert;

		public KeyCertificatePair(PrivateKey privateKey, X509Certificate publicKeyCert) {
			this.privateKey = privateKey;
			this.publicKeyCert = publicKeyCert;
		}
	}

	private static KeyCertificatePair loadDefaultKey() {
		File path = new File(getCwd(), KEYSTORE_FILE);
		if (path.exists()) {
			return loadKey(path, KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD);
		}
		return null;
	}

	private static KeyCertificatePair loadKey(File file, String storePassword, String alias, String keyPassword) {
		try {
			final KeyStore store = KeyStore.getInstance("BKS", "SC");
			store.load(new FileInputStream(file), storePassword.toCharArray());

			Enumeration<String> aliases = store.aliases();
			while(aliases.hasMoreElements()) {
				System.out.println("Alias: " + aliases.nextElement());
			}

			final X509Certificate publicKeyCert = (X509Certificate) store.getCertificate(alias);
			final PrivateKey privateKey = (PrivateKey) store.getKey(alias, keyPassword.toCharArray());

			System.out.println("Got privateKey: " + privateKey);
			System.out.println("Got certificate: " + publicKeyCert);

			return new KeyCertificatePair(privateKey, publicKeyCert);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}

		return null;
	}

}
