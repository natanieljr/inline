package com.srt.appguard.monitor.policy.impl.hardware;

import android.media.MediaRecorder;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class CameraPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final CameraPolicy instance;
	static {
		instance = new CameraPolicy();
	}

	private CameraPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CAMERA;
	}

	@MapSignatures({
		"Landroid/hardware/Camera;->open()",
		"Landroid/hardware/Camera;->open(I)"
	})
	public void android_hardware_Camera__open() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CAMERA);
		} else {
			Logger.deny(android.Manifest.permission.CAMERA);
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/media/MediaRecorder;->setVideoSource(I)"
	})
	public void android_media_MediaRecorder__setVideoSource(final MediaRecorder _this) throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.CAMERA);
		} else {
			Logger.deny(android.Manifest.permission.CAMERA);
			throw new MonitorException();
		}
	}
}
