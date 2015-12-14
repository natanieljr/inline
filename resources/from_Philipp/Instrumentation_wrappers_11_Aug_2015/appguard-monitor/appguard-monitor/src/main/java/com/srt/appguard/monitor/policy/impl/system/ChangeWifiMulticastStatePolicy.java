package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class ChangeWifiMulticastStatePolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final ChangeWifiMulticastStatePolicy instance;

	static {
		instance = new ChangeWifiMulticastStatePolicy();
	}

	private ChangeWifiMulticastStatePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;
	}

	@MapSignatures({
			"Landroid/net/wifi/WifiManager$MulticastLock;->acquire()", // returns V
			"Landroid/net/wifi/WifiManager$MulticastLock;->release()", // returns V
			"Landroid/net/wifi/WifiManager;->initializeMulticastFiltering()", // returns V

			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->acquireMulticastLock(Landroid/os/IBinder;Ljava/lang/String;)", // returns V
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->releaseMulticastLock()", // returns V
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->initializeMulticastFiltering()" // returns Z
	})
	public void android_net_WifiManager_$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE);
			// return false for IWifiManager$Stub$Proxy;->initializeMulticastFiltering()
			throw new MonitorException(false);
		}
	}
}
