package com.srt.appguard.monitor.policy.impl.location;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class MockLocationPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final MockLocationPolicy instance;
	static {
		instance = new MockLocationPolicy();
	}
	
	private MockLocationPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.ACCESS_MOCK_LOCATION;
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->addTestProvider(Ljava/lang/String;ZZZZZZZII)", // returns V
		"Landroid/location/LocationManager;->clearTestProviderEnabled(Ljava/lang/String;)", // returns V
		"Landroid/location/LocationManager;->clearTestProviderLocation(Ljava/lang/String;)", // returns V
		"Landroid/location/LocationManager;->clearTestProviderStatus(Ljava/lang/String;)", // returns V
		"Landroid/location/LocationManager;->removeTestProvider(Ljava/lang/String;)", // returns V
		"Landroid/location/LocationManager;->setTestProviderEnabled(Ljava/lang/String;Z)", // returns V
		"Landroid/location/LocationManager;->setTestProviderLocation(Ljava/lang/String;Landroid/location/Location;)", // returns V
		"Landroid/location/LocationManager;->setTestProviderStatus(Ljava/lang/String;ILandroid/os/Bundle;J)" // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->addTestProvider(Ljava/lang/String;ZZZZZZZII)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->clearTestProviderEnabled(Ljava/lang/String;)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->clearTestProviderLocation(Ljava/lang/String;)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->clearTestProviderStatus(Ljava/lang/String;)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->removeTestProvider(Ljava/lang/String;)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->setTestProviderEnabled(Ljava/lang/String;Z)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->setTestProviderLocation(Ljava/lang/String;Landroid/location/Location;)", // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->setTestProviderStatus(Ljava/lang/String;ILandroid/os/Bundle;J)" // returns V
	})
	public void mockLocation__$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.ACCESS_MOCK_LOCATION);
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_MOCK_LOCATION);
			throw new MonitorException();
		}
	}
}
