package com.srt.appguard.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.srt.appguard.monitor.AppGuardProperties;

import dalvik.system.DexClassLoader;

public class MonitorLoader {

	private static final String APPGUARD_LOG_TAG = "BaseAppGuardApplication";

	private static final String ASSETS_APPGUARD_PROPERTIES = "appguard/appguard.properties";

	private Class<?> monitorClass;
	private Object monitorInstance;
	private Application appApplication;

	public void startAppGuardMonitor(Context context) {
		try {
			InputStream propertiesIn = context.getAssets().open(ASSETS_APPGUARD_PROPERTIES);
			AppGuardProperties properties = new AppGuardProperties(propertiesIn);

			monitorClass = loadMonitor(context, properties.getMonitorApkPath(), properties.getMonitorClass());
			if (monitorClass != null) {
				// Create Monitor instance
				monitorInstance = monitorClass.newInstance();
				
				final Method initMethod = monitorClass.getDeclaredMethod("init", Context.class);
				initMethod.invoke(monitorInstance, context);
			}

			appApplication = AppDexLoader.init(context, properties.getAppApplicationClass());
		} catch (Throwable e) {
			Log.w(APPGUARD_LOG_TAG, "Error while initializing monitor.", e);
		}
	}

	public void onCreateApplication(Application oldApp) {
		if (appApplication != null) {
			AppDexLoader.replaceApplicationClass(oldApp, appApplication);
			appApplication.onCreate();
		}
	}

	private Class<?> loadMonitor(Context context, String monitorApkPath, String monitorClass) {
		File monitorApk = new File(monitorApkPath);
		File odexDir = new File(getFilesDir(context), "appguard_monitor_odex");
		if (!odexDir.exists()) {
			odexDir.mkdirs();
		}
		
		File libsDir = new File(getFilesDir(context), "appguard_monitor_libs");
		if (!libsDir.exists()) {
			libsDir.mkdirs();
		}

		if (monitorApk.exists() && odexDir.exists() && libsDir.exists()) {
			extractNativeLibs(monitorApk.getAbsolutePath(), libsDir);

			//String libraryPath = getLibraryPath(libsDir.getAbsolutePath());
			ClassLoader classLoader = new DexClassLoader(monitorApk.getAbsolutePath(), odexDir.getAbsolutePath(), libsDir.getAbsolutePath(), Thread.currentThread().getContextClassLoader());
			try {
				// Get Monitor instance
				return classLoader.loadClass(monitorClass);
			} catch (Throwable e) {
				Log.w(APPGUARD_LOG_TAG, "Error while loading monitor classes.", e);
			}
		} else {
			Log.w(APPGUARD_LOG_TAG, "Monitor package does not exist (" + monitorApk + ")");
		}

		return null;
	}

	private static void extractNativeLibs(String apk, File libsDir) {
		try {
			// Copy libs for this architecture
			ZipInputStream in = new ZipInputStream(new FileInputStream(apk));

			ZipEntry entry = in.getNextEntry();
			while (entry != null) {
				final String name = entry.getName();
				
				final File target;
				if (Build.CPU_ABI != null && name.startsWith("lib/" + Build.CPU_ABI)) {
					target = new File(libsDir, name.substring(4 + Build.CPU_ABI.length()));
				} else if (Build.CPU_ABI2 != null && name.startsWith("lib/" + Build.CPU_ABI2)) {
					target = new File(libsDir, name.substring(4 + Build.CPU_ABI2.length()));
				} else {
					entry = in.getNextEntry();
					continue;
				}
				
				target.getParentFile().mkdirs();
				if (!target.getParentFile().isDirectory())
					throw new IOException("Failed to mkdirs: "+target.getParentFile().getAbsolutePath());
				System.out.println("Extracting " + name + " to: " + target.getAbsolutePath());
				final FileOutputStream out = new FileOutputStream(target);

				int read;
				byte[] buf = new byte[1024];
				while ((read = in.read(buf)) != -1) {
					out.write(buf, 0, read);
				}
				
				out.close();
				in.closeEntry();
				
				entry = in.getNextEntry();
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getLibraryPath(String libsDir) {
		StringBuilder libraryPath = new StringBuilder();
		if (Build.CPU_ABI != null) {
			File libDir1 = new File(libsDir, Build.CPU_ABI);
			if (libDir1.exists()) {
				libraryPath.append(libDir1.getAbsolutePath());
			}
		}
		if (Build.CPU_ABI2 != null) {
			File libDir2 = new File(libsDir, Build.CPU_ABI2);
			if (libDir2.exists()) {
				if (libraryPath.length() != 0) {
					libraryPath.append(File.pathSeparatorChar);
				}
				libraryPath.append(libDir2.getAbsolutePath());
			}
		}
		return libraryPath.toString();
	}

	private File getFilesDir(Context context) {
		return context.getFilesDir();
	}

}
