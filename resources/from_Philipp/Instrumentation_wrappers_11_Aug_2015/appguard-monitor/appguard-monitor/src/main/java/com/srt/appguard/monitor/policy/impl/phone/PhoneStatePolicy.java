package com.srt.appguard.monitor.policy.impl.phone;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class PhoneStatePolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static PhoneStatePolicy instance;

	static {
		instance = new PhoneStatePolicy();
	}

	private PhoneStatePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.READ_PHONE_STATE;
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getDeviceId()"
	})
	public void android_telephony_TelephonyManager__getDeviceId() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.DeviceId()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.DeviceId()"));
			throw new MonitorException("foobar");
		}
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getDeviceSoftwareVersion()"
	})
	public void android_telephony_TelephonyManager__getDeviceSoftwareVersion() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.DeviceSoftwareVersion()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.DeviceSoftwareVersion()"));
			throw new MonitorException("1.0");
		}
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getLine1Number()"
	})
	public void android_telephony_TelephonyManager__getLine1Number() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.Line1Number()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.Line1Number()"));
			throw new MonitorException("12345");
		}
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getSimSerialNumber()"
	})
	public void android_telephony_TelephonyManager__getSimSerialNumber() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.SimSerialNumber()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.SimSerialNumber()"));
			throw new MonitorException("12345");
		}
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getSubscriberId()"
	})
	public void android_telephony_TelephonyManager__getSubscriberId() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.SubscriberId()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.SubscriberId()"));
			throw new MonitorException("12345");
		}
	}


	//newly added

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getVoiceMailAlphaTag()"
	})
	public void android_telephony_TelephonyManager__getVoiceMailAlphaTag() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.getVoiceMailAlphaTag()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.SgetVoiceMailAlphaTag()"));
			throw new MonitorException("12345");
		}
	}

	@MapSignatures({
			"Landroid/telephony/TelephonyManager;->getVoiceMailNumber()"
	})
	public void android_telephony_TelephonyManager__getVoiceMailNumber() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.getVoiceMailNumber()"));
		} else {
			Logger.deny(android.Manifest.permission.READ_PHONE_STATE, new Meta(MetaType.METHOD, "TelephonyManager.getVoiceMailNumber()"));
			throw new MonitorException("12345");
		}
	}

	/*
	@MapSignatures({
		"Landroid/telephony/TelephonyManager;->listen(Landroid/telephony/PhoneStateListener;I)V"
	})
	public void android_telephony_TelephonyManager__listen(TelephonyManager _this, PhoneStateListener listener, int events) throws Exception {
		if (allowed) {
		}
	}
	*/
}
