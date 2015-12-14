package com.srt.appguard.monitor;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.multidex.AppDexLoader;
import com.srt.appguard.monitor.policy.Policy;
import com.srt.appguard.monitor.policy.PolicyFactory;

import java.io.File;
import java.util.ArrayList;

public class Monitor {

	private static final String TAG = "Monitor";

	public static final Monitor instance = new Monitor();

	private final MonitorConfig config;
	private final ArrayList<Policy> policies;
	private String configDir;
	private String packageName;

	private static boolean guarded = false;
	private boolean initialized = false;
	private boolean appCreated = false;
	private boolean appReplaced = false;

	private Application appApplication;

	private Monitor() {
		config = new MonitorConfig();
		policies = new ArrayList<>(PolicyFactory.getPolicies());
	}

	@SuppressWarnings("UnusedDeclaration")
	public static void guard() {
		// Moved to init() method
	}

	public Application getAppApplication() {
		return appApplication;
	}

	/**
	 * Old init for backward compatibility.
	 * <p/>
	 * NOTE: NEVER EVER CHANGE METHOD NAME OR SIGNATURE!!!
	 */
	public void init(Context context, String configDir, String appguardPackageName, String serviceClass) {
		init(context, configDir, appguardPackageName, serviceClass, context.getPackageName());
	}

	/**
	 * NOTE: NEVER EVER CHANGE METHOD NAME OR SIGNATURE!!!
	 */
	public void init(Context context, String configDir, String appguardPackageName, String serviceClass, String packageName) {
		if (!guarded) {
			Log.d(TAG, "Loading app.dex...");
			AppDexLoader.loadAppDex(context);

			Log.i(TAG, "Guarding methods...");

			// Check if monitoring is disabled globally
			final File monitorFile = new File(configDir, "monitor_disabled");
			if (!monitorFile.exists()) {
				// Guarding methods
				final int numGuards = MonitorInitializer.init();

				Log.d(TAG, "Monitoring " + numGuards + " methods.");
				Logger.debug("Monitoring " + numGuards + " methods.");

				// Done
				guarded = true;
			} else {
				Log.d(TAG, "Monitoring globally disabled!");
				Logger.error("Monitoring globally disabled!");
			}
		}

		if (!initialized) {
			Log.d(TAG, "Initialyzing Monitor...");

			// Init fields
			this.configDir = configDir;
			this.packageName = packageName;

			// Create Logger
			Logger.create(context, appguardPackageName, serviceClass);
			Logger.info("Monitor initialized (" + packageName + ")");

			// Load Monitor config
			refreshConfig();

			// Give policies the chance to initialize stuff with the context
			for (final Policy policy : policies) {
				policy.init(context);
			}

			// Done
			initialized = true;
		}
	}

	/**
	 * NOTE: NEVER EVER CHANGE METHOD NAME OR SIGNATURE!!!
	 */
	@SuppressWarnings("UnusedDeclaration")
	public void createAppApplication(Context context, String appApplicationClassName) {
		if (appCreated) {
			return;
		}

		if (appApplicationClassName != null) {
			// Create original application class and register it
			appApplication = AppDexLoader.createOriginalApplicationClass(context, appApplicationClassName);
		}

		// Done
		appCreated = true;
	}

	/**
	 * NOTE: NEVER EVER CHANGE METHOD NAME OR SIGNATURE!!!
	 */
	@SuppressWarnings("UnusedDeclaration")
	public void replaceApplication(Application oldApp) {
		if (appReplaced) {
			return;
		}

		if (appApplication != null) {
			AppDexLoader.replaceApplicationClass(oldApp, appApplication);
			appApplication.onCreate();
		}

		// Done
		appReplaced = true;
	}

	public void refreshConfig() {
		if (readConfig()) {
			for (final Policy policy : policies) {
				policy.loadConfig(config);
			}
		}
	}

	private boolean readConfig() {
		try {
			File file = new File(configDir, packageName + ".xml");

			if (file.lastModified() > config.getLatestReadVersion()) {
				Logger.debug("Updating configuration for " + packageName);
				config.read(file);
				return true;
			} else {
				Logger.debug("Configuration already up to date for " + packageName);
				return false;
			}
		} catch (Exception e) {
			Logger.warn("No configuration found, using defaults");
			return false;
		}
	}
}
