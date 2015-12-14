package com.srt.appguard.monitor.policy.impl.location;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.srt.appguard.monitor.MonitorConfig;
import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.Policy;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;

import java.util.LinkedList;

public class LocationPolicy extends Policy {

	/* All Policies are required to be singletons. */
	public static final LocationPolicy instance;
	static {
		instance = new LocationPolicy();
	}

	private boolean coarseAllowed = true;
	private boolean fineAllowed = true;

	public LocationPolicy() {
	}

	@Override
	protected void loadConfig(MonitorConfig config, boolean defaultValue) {
		coarseAllowed = config.getPermissionStatus(android.Manifest.permission.ACCESS_COARSE_LOCATION, defaultValue);
		fineAllowed = config.getPermissionStatus(android.Manifest.permission.ACCESS_FINE_LOCATION, defaultValue);
	}

	@Override
	public String[] getPermissions() {
		return new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION };
	}
	
	@MapSignatures({
		"Landroid/location/LocationManager;->getBestProvider(Landroid/location/Criteria;Z)" // returns Ljava/lang/String;
	})
	public void android_location_LocationManager__getBestProvider() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getBestProvider()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getBestProvider()"));
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->getLastKnownLocation(Ljava/lang/String;)" // returns Landroid/location/Location;
		//"Landroid/location/ILocationManager$Stub$Proxy;->getLastKnownLocation(Ljava/lang/String;)", // returns Landroid/location/Location;
		//"Landroid/location/ILocationManager$Stub$Proxy;->getLastLocation(Landroid/location/LocationRequest;Ljava/lang/String;)" // returns Landroid/location/Location;
	})
	public void android_location_LocationManager__getLastKnownLocation() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getLastKnownLocation()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getLastKnownLocation()"));
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->getProvider(Ljava/lang/String;)" // returns Landroid/location/LocationProvider;
	})
	public void android_location_LocationManager__getProvider() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getProvider()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getProvider()"));
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->getProviders(Z)", // returns Ljava/util/List;
		"Landroid/location/LocationManager;->getProviders(Landroid/location/Criteria;Z)" // returns Ljava/util/List;
	})
	public void android_location_LocationManager__getProviders() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getProviders()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.getProviders()"));
			throw new MonitorException(new LinkedList<String>());
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->isProviderEnabled(Ljava/lang/String;)" // returns Z
		//"Landroid/location/ILocationManager$Stub$Proxy;->isProviderEnabled(Ljava/lang/String;)" // returns Z
	})
	public void android_location_LocationManager__isProviderEnabled() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.isProviderEnabled()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.isProviderEnabled()"));
			throw new MonitorException(false);
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/app/PendingIntent;)", // returns V
		"Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/location/LocationListener;)", // returns V
		"Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/location/LocationListener;Landroid/os/Looper;)", // returns V
		"Landroid/location/LocationManager;->requestLocationUpdates(JFLandroid/location/Criteria;Landroid/app/PendingIntent;)",
		"Landroid/location/LocationManager;->requestLocationUpdates(JFLandroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)",

		"Landroid/location/LocationManager;->requestSingleUpdate(Ljava/lang/String;Landroid/app/PendingIntent;)",
		"Landroid/location/LocationManager;->requestSingleUpdate(Ljava/lang/String;Landroid/location/LocationListener;Landroid/os/Looper;)",
		"Landroid/location/LocationManager;->requestSingleUpdate(Landroid/location/Criteria;Landroid/app/PendingIntent;)",
		"Landroid/location/LocationManager;->requestSingleUpdate(Landroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)"
	})
	public void android_location_LocationManager__requestLocationUpdates() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.requestLocationUpdates()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.requestLocationUpdates()"));
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->addGpsStatusListener(Landroid/location/GpsStatus$Listener;)" // returns Z
		//"Landroid/location/ILocationManager$Stub$Proxy;->addGpsStatusListener(Landroid/location/IGpsStatusListener;)" // returns Z
	})
	public void android_location_LocationManager__addGpsStatusListener() throws Exception {
		if (fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addGpsStatusListener()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addGpsStatusListener()"));
			throw new MonitorException(false);
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->addProximityAlert(DDFJLandroid/app/PendingIntent;)" // returns V
		//"Landroid/location/ILocationManager$Stub$Proxy;->addProximityAlert(DDFJLandroid/app/PendingIntent;)" // returns V
	})
	public void android_location_LocationManager__addProximityAlert() throws Exception {
		if (fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addProximityAlert()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addProximityAlert()"));
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/location/LocationManager;->addNmeaListener(Landroid/location/GpsStatus$NmeaListener;)" // returns Z
	})
	public void android_location_LocationManager__addNmeaListener() throws Exception {
		if (fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addNmeaListener()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.addNmeaListener()"));
			throw new MonitorException(false);
		}
	}

	
	@MapSignatures({
		"Landroid/location/LocationManager;->sendExtraCommand(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)" // returns Z
		//"Landroid/location/ILocationManager$Stub$Proxy;->sendExtraCommand(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)" // returns Z
	})
	public void android_location_LocationManager__sendExtraCommand() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.sendExtraCommand()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "LocationManager.sendExtraCommand()"));
			throw new MonitorException(false);
		}
	}	
	
	
	@MapSignatures({
		"Landroid/telephony/TelephonyManager;->getNeighboringCellInfo()" // returns Ljava/util/List;
		//"Lcom/android/internal/telephony/ITelephony$Stub$Proxy;->getNeighboringCellInfo()" // returns Ljava/util/List;
	})
	public void android_telephony_TelephonyManager__getNeighboringCellInfo() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.getNeighboringCellInfo()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.getNeighboringCellInfo()"));
			throw new MonitorException(null); //new LinkedList<NeighboringCellInfo>()
		}
	}

	@MapSignatures({
		"Landroid/telephony/TelephonyManager;->getCellLocation()" // returns Landroid/telephony/CellLocation;
		//"Lcom/android/internal/telephony/ITelephony$Stub$Proxy;->getCellLocation()" // returns Landroid/os/Bundle;
	})
	public void android_telephony_TelephonyManager__getCellLocation() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.getCellLocation()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.getCellLocation()"));
			throw new MonitorException(null); //new CellLocation() {};
		}
	}
	

	@MapSignatures({
		"Landroid/telephony/TelephonyManager;->listen(Landroid/telephony/PhoneStateListener;I)" // returns V
		// "Lcom/android/internal/telephony/ITelephonyRegistry$Stub$Proxy;->listen(Ljava/lang/String;Lcom/android/internal/telephony/IPhoneStateListener;IZ)", // returns V
	})
	public void android_telephony_TelephonyManager__listen(TelephonyManager _this, PhoneStateListener listener, int events) throws Exception {
		listener.onCellLocationChanged(new CellLocation() {});

		if ((events & PhoneStateListener.LISTEN_CELL_LOCATION) != 0) {
			if (coarseAllowed || fineAllowed) {
				Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.listen()"));
			} else {
				Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "TelephonyManager.listen()"));
				throw new MonitorException();
			}
		}
	}
	
	/*
	@MapSignatures({
		"Landroid/webkit/GeolocationService;->setEnableGps(Z)" // returns V
	})
	public void android_webkit_GeolocationService__setEnableGps() throws Exception {
		if (coarseAllowed || fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "GeolocationService.setEnableGps()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_COARSE_LOCATION, new Meta(MetaType.METHOD, "GeolocationService.setEnableGps()"));
			throw new MonitorException();
		}
	}
	
	@MapSignatures({
		"Landroid/webkit/GeolocationService;->start()" // returns Z
	})
	public void android_webkit_GeolocationService__start() throws Exception {
		if (fineAllowed) {
			Logger.allow(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "GeolocationService.start()"));
		} else {
			Logger.deny(android.Manifest.permission.ACCESS_FINE_LOCATION, new Meta(MetaType.METHOD, "GeolocationService.start()"));
			throw new MonitorException();
		}
	}
	*/

	//"Landroid/location/ILocationManager$Stub$Proxy;->getProviderInfo(Ljava/lang/String;)", // returns Landroid/os/Bundle;
	// android.webkit.WebChromeClient.onGeolocationPermissionsShowPrompt(java.lang.String,android.webkit.GeolocationPermissions.Callback)
}
