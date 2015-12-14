package com.srt.appguard.monitor.policy.impl.content;


public class BrowserPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final BrowserPolicy instance;
	static {
		instance = new BrowserPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"com.android.browser",
				"browser"
		};
	}

	@Override
	protected String getReadPermission() {
		return android.Manifest.permission.READ_HISTORY_BOOKMARKS;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_HISTORY_BOOKMARKS;
	}
}
