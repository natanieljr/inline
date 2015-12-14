package com.srt.appguard.monitor.policy.impl.content;


public class DownloadsPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final DownloadsPolicy instance;
	static {
		instance = new DownloadsPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"downloads"
		};
	}
	
	@Override
	protected String getReadPermission() {
		return "android.permission.ACCESS_DOWNLOAD_MANAGER";
	}

	@Override
	protected String getWritePermission() {
		return "android.permission.ACCESS_DOWNLOAD_MANAGER";
	}
}
