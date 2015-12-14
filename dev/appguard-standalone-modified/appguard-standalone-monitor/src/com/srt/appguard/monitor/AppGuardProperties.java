package com.srt.appguard.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class AppGuardProperties {

	private static final String PROP_MONITOR_APK_PATH = "monitorApkPath";
	private static final String PROP_MONITOR_CLASS = "monitorClass";
	private static final String PROP_APP_APPLICATION_CLASS = "appApplicationClass";

	private final Properties properties;

	/**
	 * Create new application properties.
	 * 
	 * @param monitorApkPath
	 *            Path to monitor.apk.
	 * @param monitorLibsPath
	 *            Path to monitor_libs directory.
	 * @param monitorClass
	 *            Full class name of the Monitor class.
	 */
	public AppGuardProperties(String monitorApkPath, String monitorClass) {
		properties = new Properties();
		properties.setProperty(PROP_MONITOR_APK_PATH, monitorApkPath);
		properties.setProperty(PROP_MONITOR_CLASS, monitorClass);
	}

	public AppGuardProperties(InputStream in) throws InvalidPropertiesFormatException, IOException {
		properties = new Properties();
		properties.loadFromXML(in);
	}

	public String getMonitorApkPath() {
		return properties.getProperty(PROP_MONITOR_APK_PATH);
	}

	public String getMonitorClass() {
		return properties.getProperty(PROP_MONITOR_CLASS);
	}

	public String getAppApplicationClass() {
		return properties.getProperty(PROP_APP_APPLICATION_CLASS);
	}

	public Properties getProperties() {
		return properties;
	}
}
