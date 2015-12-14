package com.srt.appguard.loader;

import android.app.Application;
import android.content.Context;

public class MonitorLoaderApplication extends Application {

	private final MonitorLoader monitorLoader = new MonitorLoader();

	@Override
	protected void attachBaseContext(Context base) {
		// This method gets called as soon as the application class is created.
		monitorLoader.startAppGuardMonitor(base);
		super.attachBaseContext(base);
	}

	@Override
	public void onCreate() {
		monitorLoader.onCreateApplication(this);
		super.onCreate();
	}
}
