package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class ChangeWifiStatePolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final ChangeWifiStatePolicy instance;

	static {
		instance = new ChangeWifiStatePolicy();
	}

	private ChangeWifiStatePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CHANGE_WIFI_STATE;
	}

	/*
	@MapSignatures({
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->addOrUpdateNetwork(Landroid/net/wifi/WifiConfiguration;)", // returns I
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->disconnect()", // returns V
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->reassociate()", // returns V
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->reconnect()", // returns V
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->setWifiApEnabled(Landroid/net/wifi/WifiConfiguration;Z)", // returns V
		"Landroid/net/wifi/IWifiManager$Stub$Proxy;->startScan(Z)" // returns V
	})
	public void android_net_WifiManager_$catchAll_V() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_WIFI_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_WIFI_STATE);
			throw new MonitorException();
		}
	}
	*/

	@MapSignatures({
			"Landroid/net/ConnectivityManager;->setRadio(IZ)", // returns Z
			"Landroid/net/ConnectivityManager;->setRadios(Z)", // returns Z
			//"Landroid/net/IConnectivityManager$Stub$Proxy;->setRadio(IZ)", // returns Z
			//"Landroid/net/IConnectivityManager$Stub$Proxy;->setRadios(Z)", // returns Z

			"Landroid/net/wifi/WifiManager;->disableNetwork(I)", // returns Z
			"Landroid/net/wifi/WifiManager;->disconnect()", // returns Z
			"Landroid/net/wifi/WifiManager;->enableNetwork(IZ)", // returns Z
			"Landroid/net/wifi/WifiManager;->pingSupplicant()", // returns Z
			"Landroid/net/wifi/WifiManager;->reassociate()", // returns Z
			"Landroid/net/wifi/WifiManager;->reconnect()", // returns Z
			"Landroid/net/wifi/WifiManager;->removeNetwork(I)", // returns Z
			"Landroid/net/wifi/WifiManager;->saveConfiguration()", // returns Z
			"Landroid/net/wifi/WifiManager;->setWifiApEnabled(Landroid/net/wifi/WifiConfiguration;Z)", // returns Z
			"Landroid/net/wifi/WifiManager;->setWifiEnabled(Z)", // returns Z
			"Landroid/net/wifi/WifiManager;->startScan()", // returns Z
			"Landroid/net/wifi/WifiManager;->startScanActive()" // returns Z

			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->disableNetwork(I)", // returns Z
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->enableNetwork(IZ)", // returns Z
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->pingSupplicant()", // returns Z
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->removeNetwork(I)", // returns Z
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->saveConfiguration()", // returns Z
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->setWifiEnabled(Z)" // returns Z
	})
	public void android_net_WifiManager_$catchAll_Z() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_WIFI_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_WIFI_STATE);
			throw new MonitorException(false);
		}
	}

	@MapSignatures({
			"Landroid/net/wifi/WifiManager;->addNetwork(Landroid/net/wifi/WifiConfiguration;)" // returns I
	})
	public void android_net_WifiManager_$addNetwork() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_WIFI_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_WIFI_STATE);
			throw new MonitorException(-1);
		}
	}

	// Added with AppGuard 2.2.7
	@MapSignatures({
			"Landroid/net/ConnectivityManager;->setRadio(ILjava/lang/String;);I", // returns I
			"Landroid/net/ConnectivityManager;->setRadios(ILjava/lang/String;);I" // returns I
	})
	public void android_net_ConnectivityManager_$catchAll_I() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CHANGE_WIFI_STATE);
		} else {
			Logger.deny(android.Manifest.permission.CHANGE_WIFI_STATE);
			throw new MonitorException(-1);
		}
	}

}
