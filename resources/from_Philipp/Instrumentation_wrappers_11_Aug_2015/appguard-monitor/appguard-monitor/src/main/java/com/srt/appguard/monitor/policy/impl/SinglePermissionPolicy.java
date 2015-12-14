package com.srt.appguard.monitor.policy.impl;

import com.srt.appguard.monitor.MonitorConfig;
import com.srt.appguard.monitor.policy.Policy;

public abstract class SinglePermissionPolicy extends Policy {

	protected boolean allowed = true;

	protected abstract String getPermissionName();

	@Override
	protected void loadConfig(MonitorConfig config, boolean defaultValue) {
		allowed = config.getPermissionStatus(getPermissionName(), defaultValue);
	}

	@Override
	public final String[] getPermissions() {
		return new String[]{getPermissionName()};
	}
}
