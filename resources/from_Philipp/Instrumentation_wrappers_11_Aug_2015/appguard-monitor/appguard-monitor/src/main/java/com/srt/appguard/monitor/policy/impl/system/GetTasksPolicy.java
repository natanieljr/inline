package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

import java.util.LinkedList;

public class GetTasksPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final GetTasksPolicy instance;

	static {
		instance = new GetTasksPolicy();
	}

	private GetTasksPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.GET_TASKS;
	}

	@MapSignatures({
			"Landroid/app/ActivityManager;->getRecentTasks(II)", // returns Ljava/util/List;
			"Landroid/app/ActivityManager;->getRunningTasks(I)", // returns Ljava/util/List;
			"Landroid/app/ActivityManagerNative;->getRecentTasks(II);Ljava/util/List" // returns Ljava/util/List;
	})
	public void getTasks_$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.GET_TASKS);
		} else {
			Logger.deny(android.Manifest.permission.GET_TASKS);
			throw new MonitorException(new LinkedList<Object>());
		}
	}
}
