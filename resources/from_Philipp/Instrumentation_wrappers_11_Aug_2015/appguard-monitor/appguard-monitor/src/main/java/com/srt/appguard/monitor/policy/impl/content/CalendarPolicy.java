package com.srt.appguard.monitor.policy.impl.content;


public class CalendarPolicy extends ContentPolicy {

	/* All Policies are required to be singletons. */
	public static final CalendarPolicy instance;
	static {
		instance = new CalendarPolicy();
	}

	@Override
	protected String[] getProtectedAuthorities() {
		return new String[] {
				"com.android.calendar",
				"calendar"
		};
	}

	@Override
	protected String getReadPermission() {
		return android.Manifest.permission.READ_CALENDAR;
	}

	@Override
	protected String getWritePermission() {
		return android.Manifest.permission.WRITE_CALENDAR;
	}
}
