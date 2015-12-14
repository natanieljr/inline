package com.srt.appguard.monitor.policy.impl.content;


public class ContactsPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static ContactsPolicy instance;
	static {
		instance = new ContactsPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"com.android.contacts",
				"call_log",
				"contacts"
		};
	}

	@Override
	protected String getReadPermission() {
		return android.Manifest.permission.READ_CONTACTS;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_CONTACTS;
	}
}
