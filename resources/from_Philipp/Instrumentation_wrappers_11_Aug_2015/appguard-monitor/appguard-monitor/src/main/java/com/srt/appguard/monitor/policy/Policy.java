package com.srt.appguard.monitor.policy;

import android.content.Context;

import com.srt.appguard.monitor.MonitorConfig;

public abstract class Policy {

	public static String getIdentifier(final Class<? extends Policy> policy) {
		return policy.getSimpleName();
	}

	public void loadConfig(final MonitorConfig config) {
		loadConfig(config, false);
	}

	/**
	 * Each policy has to define which data it needs from the config file.
	 *
	 * @param config       The monitor config.
	 * @param defaultValue The default value for permission properties.
	 */
	protected abstract void loadConfig(final MonitorConfig config, boolean defaultValue);

	/**
	 * Called when Application.onCreate() has been called. Sub-classes may overwrite this method.
	 *
	 * @param context The app context.
	 */
	public void init(Context context) {
		// NOP: Sub-classes may overwrite this method.
	}

	public String getIdentifier() {
		return getIdentifier(this.getClass());
	}

	public abstract String[] getPermissions();
}
