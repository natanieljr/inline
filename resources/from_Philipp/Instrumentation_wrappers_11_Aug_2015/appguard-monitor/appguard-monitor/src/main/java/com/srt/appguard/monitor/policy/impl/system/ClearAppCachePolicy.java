package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class ClearAppCachePolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final ClearAppCachePolicy instance;
	static {
		instance = new ClearAppCachePolicy();
	}
	
	private ClearAppCachePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CLEAR_APP_CACHE;
	}

	@MapSignatures({
		//"Landroid/content/pm/PackageManager;->freeStorage(JLandroid/content/IntentSender;)", // returns V
		//"Landroid/content/pm/PackageManager;->freeStorageAndNotify(JLandroid/content/pm/IPackageDataObserver;)", // returns V
		//"Landroid/content/pm/IPackageManager$Stub$Proxy;->freeStorage(JLandroid/content/IntentSender;)", // returns V
		//"Landroid/content/pm/IPackageManager$Stub$Proxy;->freeStorageAndNotify(JLandroid/content/pm/IPackageDataObserver;)" // returns V
	})
	public void clearAppCache_$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CLEAR_APP_CACHE);
		} else {
			Logger.deny(android.Manifest.permission.CLEAR_APP_CACHE);
			throw new MonitorException();
		}
	}
}
