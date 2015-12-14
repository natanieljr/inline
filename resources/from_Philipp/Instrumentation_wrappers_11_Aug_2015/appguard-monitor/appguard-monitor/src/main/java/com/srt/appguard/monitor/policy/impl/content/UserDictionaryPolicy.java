package com.srt.appguard.monitor.policy.impl.content;


public class UserDictionaryPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final UserDictionaryPolicy instance;
	static {
		instance = new UserDictionaryPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"user_dictionary"
		};
	}

	@Override
	protected String getReadPermission() {
		return android.Manifest.permission.READ_USER_DICTIONARY;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_USER_DICTIONARY;
	}
}
