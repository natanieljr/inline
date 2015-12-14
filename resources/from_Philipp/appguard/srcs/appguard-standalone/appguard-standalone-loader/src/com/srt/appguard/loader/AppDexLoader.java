package com.srt.appguard.loader;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import dalvik.system.DexFile;

public class AppDexLoader {

	private static final String TAG = "AppDexLoader";

	private static final String ASSETS_APP_DEX = "appguard/app.dex";
	private static final String FILES_APPGUARD_DIR = "appguard_dex";

	public static Application init(Context context, String appApplicationClassName) {
		try {
			File appApk = extractAppApk(context);
			fixPathList(context, appApk);

			if (appApplicationClassName != null) {
				return createOriginalApplicationClass(context, context.getPackageName(), appApplicationClassName);
			}
		} catch (IOException e) {
			Log.e(TAG, "Loading app.dex failed!", e);
		}
		return null;
	}

	private static File extractAppApk(Context context) throws IOException {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			File appGuardFilesDir = context.getDir(FILES_APPGUARD_DIR, Context.MODE_PRIVATE);
			File appApk = new File(appGuardFilesDir, "app-" + packageInfo.lastUpdateTime + ".apk");

			if (!appApk.exists()) {
				Log.d(TAG, "Loading app-" + packageInfo.lastUpdateTime + ".apk");
				// Remove old app-* files
				for (File file : appGuardFilesDir.listFiles()) {
					String name = file.getName();
					if (name.startsWith("app-")) {
						file.delete();
					}
				}

				// Copy app.dex from assets into app-*.apk file
				InputStream is = new BufferedInputStream(context.getAssets().open(ASSETS_APP_DEX));
				ZipOutputStream os = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(appApk)));
				os.putNextEntry(new ZipEntry("classes.dex"));

				int read = 0;
				byte[] buffer = new byte[8192];
				while ((read = is.read(buffer)) > 0) {
					os.write(buffer, 0, read);
				}
				os.closeEntry();

				os.close();
				is.close();
			}

			return appApk;
		} catch (NameNotFoundException e) {
			throw new IOException(e);
		}
	}

	private static void fixPathList(Context context, File appApk) {
		File optDir = context.getDir("odex", Context.MODE_PRIVATE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			fixPathListICS(context, appApk, optDir);
		} else {
			fixPathListPreICS(context, appApk, optDir);
		}
	}

	private static void fixPathListPreICS(Context context, File appApk, File optDir) {
		try {
			// Get context class loader
			Class pathClClass = Class.forName("dalvik.system.PathClassLoader");
			ClassLoader pcl = context.getClassLoader();

			// Fix path field
			Field pathField = pathClClass.getDeclaredField("path");
			pathField.setAccessible(true);
			String orgPath = pathField.get(pcl).toString();
			String newPath = appApk.getAbsolutePath() + File.pathSeparator + orgPath;
			pathField.set(pcl, newPath);

			// Fix mPaths field
			Field mPathsField = pathClClass.getDeclaredField("mPaths");
			mPathsField.setAccessible(true);
			String[] newMPaths = newPath.split(":");
			mPathsField.set(pcl, newMPaths);

			// Get old mFiles, mZips and mDexs values
			Field mFilesField = pathClClass.getDeclaredField("mFiles");
			mFilesField.setAccessible(true);
			File[] orgMFiles = (File[]) mFilesField.get(pcl);
			Field mZipsField = pathClClass.getDeclaredField("mZips");
			mZipsField.setAccessible(true);
			ZipFile[] orgMZips = (ZipFile[]) mZipsField.get(pcl);
			Field mDexsField = pathClClass.getDeclaredField("mDexs");
			mDexsField.setAccessible(true);
			DexFile[] orgMDexs = (DexFile[]) mDexsField.get(pcl);

			// Create new mFiles, mZips and mDexs
			boolean wantDex = System.getProperty("android.vm.dexfile", "").equals("true");
			int length = 1 + orgMFiles.length;
			File[] newMFiles = new File[length];
			ZipFile[] newMZips = new ZipFile[length];
			DexFile[] newMDexs = new DexFile[length];
			for (int i = 0; i < length; i++) {
				if (i == 0) {
					// Add app.apk
					newMFiles[i] = appApk;
					newMZips[i] = new ZipFile(appApk);
					if (wantDex) {
						File appOdex = new File(optDir, appApk.getName() + ".odex");
						newMDexs[i] = DexFile.loadDex(appApk.getAbsolutePath(), appOdex.getAbsolutePath(), 0);
					}
				} else {
					// Copy old entries
					newMFiles[i] = orgMFiles[i - 1];
					newMZips[i] = orgMZips[i - 1];
					newMDexs[i] = orgMDexs[i - 1];
				}
			}

			// Fix mFiles, mZips and mDexs field
			mFilesField.set(pcl, newMFiles);
			mZipsField.set(pcl, newMZips);
			mDexsField.set(pcl, newMDexs);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "fixPathListPreICS failed!", e);
		} catch (NoSuchFieldException e) {
			Log.e(TAG, "fixPathListPreICS failed!", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "fixPathListPreICS failed!", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "fixPathListPreICS failed!", e);
		} catch (IOException e) {
			Log.e(TAG, "fixPathListPreICS failed!", e);
		}
	}

	private static void fixPathListICS(Context context, File appApk, File optDir) {
		try {
			// Get context class loader
			ClassLoader cl = context.getClassLoader();

			// Get DexPathList object
			Class<?> baseDexClClass = Class.forName("dalvik.system.BaseDexClassLoader");
			Field pathListField = baseDexClClass.getDeclaredField("pathList");
			pathListField.setAccessible(true);
			Object pathList = pathListField.get(cl);

			// Get dexElements
			Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
			Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
			dexElementsField.setAccessible(true);
			Object[] dexElements = (Object[]) dexElementsField.get(pathList);

			// Call makeDexElements() for appDex
			ArrayList<File> files = new ArrayList<File>();
			files.add(appApk);

			Object[] newElements = makeDexElements(dexPathListClass, files, optDir);

			// Merge old and new elements
			Class<?> elementClass = Class.forName("dalvik.system.DexPathList$Element");
			Object elementArray = Array.newInstance(elementClass, dexElements.length + newElements.length);

			int i = 0;
			for (Object o : newElements) {
				Array.set(elementArray, i++, o);
			}
			for (Object o : dexElements) {
				Array.set(elementArray, i++, o);
			}

			// Set new elements
			dexElementsField.set(pathList, elementArray);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		} catch (NoSuchFieldException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "fixPathListICS failed!", e);
		}
	}

	private static Object[] makeDexElements(Class<?> dexPathListClass, ArrayList<File> files, File optDir) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Method makeDexElementsMethod = dexPathListClass.getDeclaredMethod("makeDexElements", ArrayList.class, File.class, ArrayList.class);
			makeDexElementsMethod.setAccessible(true);
			return (Object[]) makeDexElementsMethod.invoke(null, files, optDir, new ArrayList<IOException>());
		} else {
			Method makeDexElementsMethod = dexPathListClass.getDeclaredMethod("makeDexElements", ArrayList.class, File.class);
			makeDexElementsMethod.setAccessible(true);
			return (Object[]) makeDexElementsMethod.invoke(null, files, optDir);
		}
	}

	private static Application createOriginalApplicationClass(Context contextImpl, String packageName, String appApplicationClass) throws IOException {
		try {
			// Get activityThread instance
			ActivityThreadInternal activityThread = new ActivityThreadInternal();
			Instrumentation instrumentation = activityThread.getInstrumentation();
			LoadedApkInternal loadedApk = activityThread.getLoadedApk(packageName);

			loadedApk.setApplication(null);
			loadedApk.setApplicationName(appApplicationClass);
			return loadedApk.makeApplication(contextImpl, instrumentation, appApplicationClass);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (NoSuchFieldException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (InstantiationException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		}
		return null;
	}

	public static void replaceApplicationClass(Application oldApp, Application newApp) {
		try {
			ActivityThreadInternal activityThread = new ActivityThreadInternal();
			activityThread.replaceApplication(oldApp, newApp);
			activityThread.setInitialApplication(newApp);

			LoadedApkInternal loadedApk = activityThread.getLoadedApk(oldApp.getPackageName());
			loadedApk.setApplication(newApp);
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		} catch (NoSuchFieldException e) {
			Log.e(TAG, "Creating original application class failed!", e);
		}
	}

	private static class ActivityThreadInternal {
		private final Class<?> clazz;
		private final Object instance;

		private ActivityThreadInternal() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
				InvocationTargetException {
			this.clazz = Class.forName("android.app.ActivityThread");
			Method currentActivityThreadMethod = clazz.getDeclaredMethod("currentActivityThread");
			currentActivityThreadMethod.setAccessible(true);
			this.instance = currentActivityThreadMethod.invoke(null);
		}

		@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "unchecked"})
		private LoadedApkInternal getLoadedApk(String packageName) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException,
				InvocationTargetException, ClassNotFoundException {
			try {
				Method peekPackageInfoMethod = clazz.getDeclaredMethod("peekPackageInfo", String.class, boolean.class);
				peekPackageInfoMethod.setAccessible(true);
				Object loadedApk = peekPackageInfoMethod.invoke(instance, packageName, true);
				return new LoadedApkInternal(loadedApk);
			} catch (NoSuchMethodException e) {
				// Pre-ICS
				Field mPackagesField = clazz.getDeclaredField("mPackages");
				mPackagesField.setAccessible(true);
				Map<String, WeakReference<Object>> mPackages = (Map<String, WeakReference<Object>>) mPackagesField.get(instance);
				synchronized (mPackages) {
					WeakReference<Object> ref = mPackages.get(packageName);
					Object loadedApk = ref != null ? ref.get() : null;
					return new LoadedApkInternal(loadedApk);
				}
			}
		}

		private Instrumentation getInstrumentation() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, ClassNotFoundException {
			Field instrumentationField = clazz.getDeclaredField("mInstrumentation");
			instrumentationField.setAccessible(true);
			return (Instrumentation) instrumentationField.get(instance);
		}

		private void replaceApplication(Application oldApp, Application newApp) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
			Field allApplicationsField = clazz.getDeclaredField("mAllApplications");
			allApplicationsField.setAccessible(true);
			@SuppressWarnings("unchecked")
			ArrayList<Application> allApplications = (ArrayList<Application>) allApplicationsField.get(instance);
			allApplications.remove(oldApp);
			allApplications.add(newApp);
		}

		private void setInitialApplication(Application app) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
			Field initialApplicationField = clazz.getDeclaredField("mInitialApplication");
			initialApplicationField.setAccessible(true);
			initialApplicationField.set(instance, app);
		}
	}

	private static class LoadedApkInternal {
		private final Class<?> clazz;
		private final Object instance;

		private LoadedApkInternal(Object loadedApk) throws ClassNotFoundException {
			this.clazz = Class.forName("android.app.LoadedApk");
			this.instance = loadedApk;
		}

		private Application makeApplication(Context appContext, Instrumentation instrumentation, String appClass) throws InstantiationException,
				IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
			ClassLoader cl = appContext.getClassLoader();
			Application app = instrumentation.newApplication(cl, appClass, appContext);

			Class<?> contextImplClass = Class.forName("android.app.ContextImpl");
			Method setOuterContextMethod = contextImplClass.getDeclaredMethod("setOuterContext", Context.class);
			setOuterContextMethod.setAccessible(true);
			setOuterContextMethod.invoke(appContext, app);

			return app;
		}

		private void setApplication(Object application) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
			Field applicationField = clazz.getDeclaredField("mApplication");
			applicationField.setAccessible(true);
			applicationField.set(instance, application);
		}

		private void setApplicationName(String applicationClassName) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
			Field applicationInfoField = clazz.getDeclaredField("mApplicationInfo");
			applicationInfoField.setAccessible(true);
			ApplicationInfo applicationInfo = (ApplicationInfo) applicationInfoField.get(instance);
			applicationInfo.className = applicationClassName;
		}
	}
}
