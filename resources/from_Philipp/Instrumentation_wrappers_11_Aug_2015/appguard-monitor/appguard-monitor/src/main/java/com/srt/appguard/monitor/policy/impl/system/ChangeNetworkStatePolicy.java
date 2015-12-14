package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class ChangeNetworkStatePolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final ChangeNetworkStatePolicy instance;
	static {
		instance = new ChangeNetworkStatePolicy();
	}

	private ChangeNetworkStatePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CHANGE_NETWORK_STATE;
	}

	@MapSignatures({
		"Landroid/net/ConnectivityManager;->setMobileDataEnabled(Z)", // returns V
		"Landroid/net/ConnectivityManager;->setNetworkPreference(I)" // returns V

		//"Landroid/net/IConnectivityManager$Stub$Proxy;->setMobileDataEnabled(Z)", // returns V
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->setNetworkPreference(I)", // returns V

		//"Landroid/os/INetworkManagementService$Stub$Proxy;->attachPppd(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->detachPppd(Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->disableNat(Ljava/lang/String;Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->enableNat(Ljava/lang/String;Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->setAccessPoint(Landroid/net/wifi/WifiConfiguration;Ljava/lang/String;Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->setInterfaceThrottle(Ljava/lang/String;II)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->setIpForwardingEnabled(Z)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->stopTethering()", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->tetherInterface(Ljava/lang/String;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->unregisterObserver(Landroid/net/INetworkManagementEventObserver;)", // returns V
		//"Landroid/os/INetworkManagementService$Stub$Proxy;->untetherInterface(Ljava/lang/String;)" // returns V
	})
	public void android_net_ConnectivityManager_$catchAll_V() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_NETWORK_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_NETWORK_STATE);
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/net/ConnectivityManager;->requestRouteToHost(II)", // returns Z
		"Landroid/net/ConnectivityManager;->setRadio(IZ)", // returns Z
		"Landroid/net/ConnectivityManager;->setRadios(Z)" // returns Z

		//"Landroid/net/IConnectivityManager$Stub$Proxy;->requestRouteToHost(II)", // returns Z
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->setRadio(IZ)", // returns Z
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->setRadios(Z)" // returns Z
	})
	public void android_net_ConnectivityManager_$catchAll_Z() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_NETWORK_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_NETWORK_STATE);
			throw new MonitorException(false);
		}
	}

	@MapSignatures({
		"Landroid/net/ConnectivityManager;->startUsingNetworkFeature(ILjava/lang/String;)", // returns I
		"Landroid/net/ConnectivityManager;->stopUsingNetworkFeature(ILjava/lang/String;)" // returns I

		//"Landroid/net/IConnectivityManager$Stub$Proxy;->stopUsingNetworkFeature(ILjava/lang/String;)", // returns I
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->stopUsingNetworkFeature(ILjava/lang/String;)" // returns I
	})
	public void android_net_ConnectivityManager_$networkFeature() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_NETWORK_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_NETWORK_STATE);
			throw new MonitorException(-1);
		}
	}
	
	@MapSignatures({
		"Landroid/net/ConnectivityManager;->tether(Ljava/lang/String;)", // returns I
		"Landroid/net/ConnectivityManager;->untether(Ljava/lang/String;)" // returns I
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->tether(Ljava/lang/String;)", // returns I
		//"Landroid/net/IConnectivityManager$Stub$Proxy;->untether(Ljava/lang/String;)" // returns I
	})
	public void android_net_ConnectivityManager_$tether() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_NETWORK_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_NETWORK_STATE);
			// check if this is the correct return value!
			throw new MonitorException(-1);
		}
	}
}
