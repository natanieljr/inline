package com.srt.appguard.monitor.policy.impl.content;


public class SmsPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final SmsPolicy instance;
	static {
		instance = new SmsPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"sms",
				"mms",
				"mms-sms"
		};
	}

	@Override
	protected String getReadPermission() {
		return android.Manifest.permission.READ_SMS;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_SMS;
	}
}
