package com.srt.appguard.monitor.policy.impl;

import android.app.Activity;

import com.srt.appguard.monitor.Monitor;
import com.srt.appguard.monitor.MonitorConfig;
import com.srt.appguard.monitor.policy.Policy;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;

public class RefreshConfigPolicy extends Policy {

	/* All Policies are required to be singletons. */
	public static final RefreshConfigPolicy instance;
	static {
		instance = new RefreshConfigPolicy();
	}

	@Override
	protected void loadConfig(final MonitorConfig config, boolean defaultValue) {
	}

	@Override
	public String[] getPermissions() {
		return new String[0];
	}

	@MapSignatures({ "Landroid/app/Activity;->onResume()" })
	public void Activity_onResume(Activity _this) throws Exception {
		Monitor.instance.refreshConfig();
	}
}
