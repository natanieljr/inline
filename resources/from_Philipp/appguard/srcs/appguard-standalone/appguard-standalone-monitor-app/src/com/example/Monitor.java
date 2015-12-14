package com.example;

import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import de.uds.infsec.instrumentation.Instrumentation;
import de.uds.infsec.instrumentation.annotation.Redirect;

public class Monitor {

	private static final String TAG = "Monitor";

	public Monitor() {
		System.out.println("Monitor constructed.");
	}
	
	public void init(Context context) {
		Instrumentation.processClass(Monitor.class);

		System.out.println("Monitor initialized for package " + context.getPackageName());
	}

	@Redirect("android.hardware.Camera->open")
	public static Camera Camera_open(int cameraId) {
		Log.i(TAG, "Camera_open() called. cameraId = " + cameraId);
		return null;
	}

	@Redirect("java.net.URL->openConnection")
	public static URLConnection URL_openConnection(URL _this) {
		Log.i(TAG, "URL_openConnection() called. _this = " + _this);
		
		class ${};
		return (URLConnection) Instrumentation.callObjectMethod($.class, _this);
	}
}
