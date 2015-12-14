package com.srt.appguard.monitor.policy.impl.content;


public class SettingsPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final SettingsPolicy instance;
	static {
		instance = new SettingsPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"settings"
		};
	}

	@Override
	protected String getReadPermission() {
		return null; //android.Manifest.permission.WRITE_SETTINGS;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_SETTINGS;
	}
}
