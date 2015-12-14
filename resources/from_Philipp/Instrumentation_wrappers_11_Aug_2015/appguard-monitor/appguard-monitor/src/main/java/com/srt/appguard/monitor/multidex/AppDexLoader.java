package com.srt.appguard.monitor.multidex;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AppDexLoader {

	public static final String ASSETS_APPGUARD_APP_DEX = "assets/appguard/app.dex";
	private static final String TAG = "AppDexLoader";

	public static Application loadAppDex(Context context) {
		// For newly inlined apps the original classes.dex is stored as classes2.dex.
		// This makes it compatible with the official MultiDex library and with native
		// MultiDex support (since libArt 2.1).
		// Backward compatibility: If appguard/app.dex exists in assets directory,
		// we also add it to the pathList.
		MultiDex.install(context, ASSETS_APPGUARD_APP_DEX);

		// We installed the additional dex files, so disable original MultiDex library
		disableOriginalMultidex();
		return null;
	}

	private static void disableOriginalMultidex() {
		try {
			final Class<?> multiDexClass = Class.forName("android.support.multidex.MultiDex");
			final Field installedApkField = multiDexClass.getDeclaredField("installedApk");
			installedApkField.setAccessible(true);

			final Collection<String> installedApks = (Collection<String>) installedApkField.get(null);
			installedApks.addAll(MultiDex.installedApk);
			Log.i(TAG, "Disabled original multidex library.");
		} catch (ClassNotFoundException e) {
			Log.i(TAG, "Original multidex library not present.");
		} catch (IllegalAccessException e) {
			Log.w(TAG, "Error while disabling original MultiDex library.", e);
		} catch (NoSuchFieldException e) {
			Log.w(TAG, "Error while disabling original MultiDex library.", e);
		}
	}

	public static Application createOriginalApplicationClass(Context context, String appApplicationClass) {
		return createOriginalApplicationClass(context, context.getPackageName(), appApplicationClass);
	}

	private static Application createOriginalApplicationClass(Context contextImpl, String packageName, String appApplicationClass) {
		try {
			// Get activityThread instance
			ActivityThreadInternal activityThread = new ActivityThreadInternal();
			Instrumentation instrumentation = activityThread.getInstrumentation();
			LoadedApkInternal loadedApk = activityThread.getLoadedApk(packageName);

			loadedApk.setApplication(null);
			loadedApk.setApplicationName(appApplicationClass);
			return loadedApk.makeApplication(contextImpl, instrumentation, appApplicationClass);
		} catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | InstantiationException e) {
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
		} catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
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
				IllegalAccessException, ClassNotFoundException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
			// Get values from given (old) ContextImpl
			Class<?> contextImplClass = Class.forName("android.app.ContextImpl");

			Field mPackageInfoField = contextImplClass.getDeclaredField("mPackageInfo");
			mPackageInfoField.setAccessible(true);
			Object mPackageInfo = mPackageInfoField.get(appContext);

			Field mMainThreadField = contextImplClass.getDeclaredField("mMainThread");
			mMainThreadField.setAccessible(true);
			Object mMainThread = mMainThreadField.get(appContext);

			// Create new ContextImpl
			Context contextImpl;
			try {
				Method createAppContextMethod = contextImplClass.getDeclaredMethod("createAppContext", Class.forName("android.app.ActivityThread"), clazz);
				createAppContextMethod.setAccessible(true);
				contextImpl = (Context) createAppContextMethod.invoke(null, mMainThread, mPackageInfo);
			} catch (NoSuchMethodException e) {
				// Pre KitKat
				Constructor<?> contextImplConstructor = contextImplClass.getDeclaredConstructor();
				contextImplConstructor.setAccessible(true);
				contextImpl = (Context) contextImplConstructor.newInstance();
				Method initMethod = contextImplClass.getDeclaredMethod("init", clazz, IBinder.class, Class.forName("android.app.ActivityThread"));
				initMethod.setAccessible(true);
				initMethod.invoke(contextImpl, mPackageInfo, null, mMainThread);
			}

			// Create application instance
			ClassLoader cl = appContext.getClassLoader();
			Application app = instrumentation.newApplication(cl, appClass, contextImpl);

			// Set outerContext of contextImpl
			Method setOuterContextMethod = contextImplClass.getDeclaredMethod("setOuterContext", Context.class);
			setOuterContextMethod.setAccessible(true);
			setOuterContextMethod.invoke(contextImpl, app);

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
