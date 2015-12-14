package com.srt.appguard.monitor.policy.impl.content;


public class MediaStorePolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final MediaStorePolicy instance;
	static {
		instance = new MediaStorePolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"media"
		};
	}

	@Override
	protected String getReadPermission() {
		return appguard.Manifest.permission.ACCESS_MEDIA_STORAGE;
	}

	@Override
	protected String getWritePermission() {
		return appguard.Manifest.permission.ACCESS_MEDIA_STORAGE;
	}
}
