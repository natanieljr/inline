/******************************************************
 * AUTO GENERATED FILE - DO NOT MODIFY
 *****************************************************/
package com.srt.appguard.monitor;

import android.app.Application;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.instrumentation.ArtInstrumentation;
import com.srt.appguard.monitor.instrumentation.DalvikInstrumentation;
import com.srt.appguard.monitor.instrumentation.Instrumentation;
import com.srt.appguard.monitor.policy.impl.hardware.CameraPolicy;
import com.srt.appguard.monitor.policy.impl.internet.InternetPolicy;
import com.srt.appguard.monitor.policy.impl.content.ContactsPolicy;
import com.srt.appguard.monitor.policy.impl.location.LocationPolicy;
import com.srt.appguard.monitor.policy.impl.phone.CallPhonePolicy;
import com.srt.appguard.monitor.policy.impl.phone.SendSmsPolicy;
import com.srt.appguard.monitor.policy.impl.phone.PhoneStatePolicy;
import com.srt.appguard.monitor.policy.impl.hardware.RecordAudioPolicy;
import com.srt.appguard.monitor.policy.impl.hardware.ModifyAudioSettingsPolicy;
import com.srt.appguard.monitor.policy.impl.content.CalendarPolicy;
import com.srt.appguard.monitor.policy.impl.content.BrowserPolicy;
import com.srt.appguard.monitor.policy.impl.content.SettingsPolicy;
import com.srt.appguard.monitor.policy.impl.content.SmsPolicy;
import com.srt.appguard.monitor.policy.impl.location.MockLocationPolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeNetworkStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeWifiMulticastStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeWifiStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.GetTasksPolicy;
import com.srt.appguard.monitor.policy.impl.system.WakeLockPolicy;
import com.srt.appguard.monitor.policy.impl.content.MediaStorePolicy;
import com.srt.appguard.monitor.policy.impl.RefreshConfigPolicy;

@SuppressWarnings({"rawtypes", "deprecation", "UnusedDeclaration", "unchecked"})
public class MonitorInitializer {

	private static Object f_0 = 0;
	private static Object f_1 = 0;
	private static Object f_2 = 0;
	private static Object f_3 = 0;
	private static Object f_4 = 0;
	private static Object f_5 = 0;
	private static Object f_6 = 0;
	private static Object f_7 = 0;
	private static Object f_8 = 0;
	private static Object f_9 = 0;
	private static Object f_10 = 0;
	private static Object f_11 = 0;
	private static Object f_12 = 0;
	private static Object f_13 = 0;
	private static Object f_14 = 0;
	private static Object f_15 = 0;
	private static Object f_16 = 0;
	private static Object f_17 = 0;
	private static Object f_18 = 0;
	private static Object f_19 = 0;
	private static Object f_20 = 0;
	private static Object f_21 = 0;
	private static Object f_22 = 0;
	private static Object f_23 = 0;
	private static Object f_24 = 0;
	private static Object f_25 = 0;
	private static Object f_26 = 0;
	private static Object f_27 = 0;
	private static Object f_28 = 0;
	private static Object f_29 = 0;
	private static Object f_30 = 0;
	private static Object f_31 = 0;
	private static Object f_32 = 0;
	private static Object f_33 = 0;
	private static Object f_34 = 0;
	private static Object f_35 = 0;
	private static Object f_36 = 0;
	private static Object f_37 = 0;
	private static Object f_38 = 0;
	private static Object f_39 = 0;
	private static Object f_40 = 0;
	private static Object f_41 = 0;
	private static Object f_42 = 0;
	private static Object f_43 = 0;
	private static Object f_44 = 0;
	private static Object f_45 = 0;
	private static Object f_46 = 0;
	private static Object f_47 = 0;
	private static Object f_48 = 0;
	private static Object f_49 = 0;
	private static Object f_50 = 0;
	private static Object f_51 = 0;
	private static Object f_52 = 0;
	private static Object f_53 = 0;
	private static Object f_54 = 0;
	private static Object f_55 = 0;
	private static Object f_56 = 0;
	private static Object f_57 = 0;
	private static Object f_58 = 0;
	private static Object f_59 = 0;
	private static Object f_60 = 0;
	private static Object f_61 = 0;
	private static Object f_62 = 0;
	private static Object f_63 = 0;
	private static Object f_64 = 0;
	private static Object f_65 = 0;
	private static Object f_66 = 0;
	private static Object f_67 = 0;
	private static Object f_68 = 0;
	private static Object f_69 = 0;
	private static Object f_70 = 0;
	private static Object f_71 = 0;
	private static Object f_72 = 0;
	private static Object f_73 = 0;
	private static Object f_74 = 0;
	private static Object f_75 = 0;
	private static Object f_76 = 0;
	private static Object f_77 = 0;
	private static Object f_78 = 0;
	private static Object f_79 = 0;
	private static Object f_80 = 0;
	private static Object f_81 = 0;
	private static Object f_82 = 0;
	private static Object f_83 = 0;
	private static Object f_84 = 0;
	private static Object f_85 = 0;
	private static Object f_86 = 0;
	private static Object f_87 = 0;
	private static Object f_88 = 0;
	private static Object f_89 = 0;
	private static Object f_90 = 0;
	private static Object f_91 = 0;
	private static Object f_92 = 0;
	private static Object f_93 = 0;
	private static Object f_94 = 0;
	private static Object f_95 = 0;
	private static Object f_96 = 0;
	private static Object f_97 = 0;
	private static Object f_98 = 0;
	private static Object f_99 = 0;
	private static Object f_100 = 0;
	private static Object f_101 = 0;
	private static Object f_102 = 0;
	private static Object f_103 = 0;
	private static Object f_104 = 0;
	private static Object f_105 = 0;
	private static Object f_106 = 0;
	private static Object f_107 = 0;
	private static Object f_108 = 0;
	private static Object f_109 = 0;
	private static Object f_110 = 0;
	private static Object f_111 = 0;
	private static Object f_112 = 0;
	private static Object f_113 = 0;
	private static Object f_114 = 0;
	private static Object f_115 = 0;
	private static Object f_116 = 0;
	private static Object f_117 = 0;
	private static Object f_118 = 0;
	private static Object f_119 = 0;
	private static Object f_120 = 0;
	private static Object f_121 = 0;
	private static Object f_122 = 0;
	private static Object f_123 = 0;
	private static Object f_124 = 0;
	private static Object f_125 = 0;
	private static Object f_126 = 0;
	private static Object f_127 = 0;
	private static Object f_128 = 0;
	private static Object f_129 = 0;
	private static Object f_130 = 0;
	private static Object f_131 = 0;
	private static Object f_132 = 0;
	private static Object f_133 = 0;
	private static Object f_134 = 0;
	private static Object f_135 = 0;
	private static Object f_136 = 0;
	private static Object f_137 = 0;
	private static Object f_138 = 0;
	private static Object f_139 = 0;
	private static Object f_140 = 0;
	private static Object f_141 = 0;
	private static Object f_142 = 0;
	private static Object f_143 = 0;
	private static Object f_144 = 0;
	private static Object f_145 = 0;
	private static Object f_146 = 0;
	private static Object f_147 = 0;
	private static Object f_148 = 0;
	private static Object f_149 = 0;
	private static Object f_150 = 0;

	private static final Instrumentation INSTRUMENTATION;

	static {
		if(isLibArt()) {
			INSTRUMENTATION = new ArtInstrumentation();
		} else {
			INSTRUMENTATION = new DalvikInstrumentation();
		}
	}

	private static boolean isLibArt() {
		final String vmVersion = System.getProperty("java.vm.version");
		return vmVersion != null && vmVersion.startsWith("2");
	}

	public static int init() {
		replaceMethods();
		return INSTRUMENTATION.getNumGuards();
	}

	private static Object f_installProviders_1 = 0;
	public static void m_install_providers_1(Object _this, android.content.Context arg0, java.util.List arg1) throws Throwable {
		Application application = Monitor.instance.getAppApplication();
		if(application != null) {
			arg0 = application;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_installProviders_1, _this, arg0, arg1);
	}

	public static void replaceMethods() {
		f_installProviders_1 = INSTRUMENTATION.hookMethod("android/app/ActivityThread", "installContentProviders", "(Landroid/content/Context;Ljava/util/List;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_install_providers_1", "(Ljava/lang/Object;Landroid/content/Context;Ljava/util/List;)V");

		f_0 = INSTRUMENTATION.hookMethod("android/app/Activity", "onResume", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_0", "(Landroid/app/Activity;)V");
		f_1 = INSTRUMENTATION.hookMethod("android/app/Activity", "startActivityForResult", "(Landroid/content/Intent;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_1", "(Landroid/app/Activity;Landroid/content/Intent;I)V");
		f_2 = INSTRUMENTATION.hookMethod("android/app/Activity", "startActivityFromChild", "(Landroid/app/Activity;Landroid/content/Intent;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_2", "(Landroid/app/Activity;Landroid/app/Activity;Landroid/content/Intent;I)V");
		f_3 = INSTRUMENTATION.hookMethod("android/app/Activity", "startActivityIfNeeded", "(Landroid/content/Intent;I)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_3", "(Landroid/app/Activity;Landroid/content/Intent;I)Z");
		f_4 = INSTRUMENTATION.hookMethod("android/app/ActivityManager", "getRecentTasks", "(II)Ljava/util/List;", "com/srt/appguard/monitor/MonitorInitializer", "m_4", "(Landroid/app/ActivityManager;II)Ljava/util/List;");
		f_5 = INSTRUMENTATION.hookMethod("android/app/ActivityManager", "getRunningTasks", "(I)Ljava/util/List;", "com/srt/appguard/monitor/MonitorInitializer", "m_5", "(Landroid/app/ActivityManager;I)Ljava/util/List;");
		f_6 = INSTRUMENTATION.hookMethod("android/app/ActivityManagerNative", "getRecentTasks", "(II)Ljava/util/List", "com/srt/appguard/monitor/MonitorInitializer", "m_6", "(Ljava/lang/Object;II)Ljava/util/List");
		f_7 = INSTRUMENTATION.hookMethod("android/bluetooth/BluetoothHeadset", "startVoiceRecognition", "(Landroid/bluetooth/BluetoothDevice;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_7", "(Landroid/bluetooth/BluetoothHeadset;Landroid/bluetooth/BluetoothDevice;)Z");
		f_8 = INSTRUMENTATION.hookMethod("android/bluetooth/BluetoothHeadset", "stopVoiceRecognition", "(Landroid/bluetooth/BluetoothDevice;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_8", "(Landroid/bluetooth/BluetoothHeadset;Landroid/bluetooth/BluetoothDevice;)Z");
		f_9 = INSTRUMENTATION.hookMethod("android/bluetooth/HeadsetBase", "acquireWakeLock", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_9", "(Ljava/lang/Object;)V");
		f_10 = INSTRUMENTATION.hookMethod("android/bluetooth/HeadsetBase", "releaseWakeLock", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_10", "(Ljava/lang/Object;)V");
		f_11 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "bulkInsert", "(Landroid/net/Uri;[Landroid/content/ContentValues;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_11", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;[Landroid/content/ContentValues;)I");
		f_12 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "delete", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_12", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
		f_13 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "insert", "(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;", "com/srt/appguard/monitor/MonitorInitializer", "m_13", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
		f_14 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "openFile", "(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;", "com/srt/appguard/monitor/MonitorInitializer", "m_14", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
		f_15 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "query", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "com/srt/appguard/monitor/MonitorInitializer", "m_15", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
		f_16 = INSTRUMENTATION.hookMethod("android/content/ContentProviderClient", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_16", "(Landroid/content/ContentProviderClient;Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
		f_17 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "bulkInsert", "(Landroid/net/Uri;[Landroid/content/ContentValues;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_17", "(Landroid/content/ContentResolver;Landroid/net/Uri;[Landroid/content/ContentValues;)I");
		f_18 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "delete", "(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_18", "(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I");
		f_19 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "insert", "(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;", "com/srt/appguard/monitor/MonitorInitializer", "m_19", "(Landroid/content/ContentResolver;Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;");
		f_20 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "openFileDescriptor", "(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;", "com/srt/appguard/monitor/MonitorInitializer", "m_20", "(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;");
		f_21 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "openInputStream", "(Landroid/net/Uri;)Ljava/io/InputStream;", "com/srt/appguard/monitor/MonitorInitializer", "m_21", "(Landroid/content/ContentResolver;Landroid/net/Uri;)Ljava/io/InputStream;");
		f_22 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "query", "(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;", "com/srt/appguard/monitor/MonitorInitializer", "m_22", "(Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;");
		f_23 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "registerContentObserver", "(Landroid/net/Uri;ZLandroid/database/ContentObserver;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_23", "(Landroid/content/ContentResolver;Landroid/net/Uri;ZLandroid/database/ContentObserver;)V");
		f_24 = INSTRUMENTATION.hookMethod("android/content/ContentResolver", "update", "(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_24", "(Landroid/content/ContentResolver;Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I");
		f_25 = INSTRUMENTATION.hookStaticMethod("android/hardware/Camera", "open", "()Landroid/hardware/Camera;", "com/srt/appguard/monitor/MonitorInitializer", "m_25", "()Landroid/hardware/Camera;");
		f_26 = INSTRUMENTATION.hookStaticMethod("android/hardware/Camera", "open", "(I)Landroid/hardware/Camera;", "com/srt/appguard/monitor/MonitorInitializer", "m_26", "(I)Landroid/hardware/Camera;");
		f_27 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "addGpsStatusListener", "(Landroid/location/GpsStatus$Listener;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_27", "(Landroid/location/LocationManager;Landroid/location/GpsStatus$Listener;)Z");
		f_28 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "addNmeaListener", "(Landroid/location/GpsStatus$NmeaListener;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_28", "(Landroid/location/LocationManager;Landroid/location/GpsStatus$NmeaListener;)Z");
		f_29 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "addProximityAlert", "(DDFJLandroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_29", "(Landroid/location/LocationManager;DDFJLandroid/app/PendingIntent;)V");
		f_30 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "addTestProvider", "(Ljava/lang/String;ZZZZZZZII)V", "com/srt/appguard/monitor/MonitorInitializer", "m_30", "(Landroid/location/LocationManager;Ljava/lang/String;ZZZZZZZII)V");
		f_31 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "clearTestProviderEnabled", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_31", "(Landroid/location/LocationManager;Ljava/lang/String;)V");
		f_32 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "clearTestProviderLocation", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_32", "(Landroid/location/LocationManager;Ljava/lang/String;)V");
		f_33 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "clearTestProviderStatus", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_33", "(Landroid/location/LocationManager;Ljava/lang/String;)V");
		f_34 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "getBestProvider", "(Landroid/location/Criteria;Z)Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_34", "(Landroid/location/LocationManager;Landroid/location/Criteria;Z)Ljava/lang/String;");
		f_35 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "getLastKnownLocation", "(Ljava/lang/String;)Landroid/location/Location;", "com/srt/appguard/monitor/MonitorInitializer", "m_35", "(Landroid/location/LocationManager;Ljava/lang/String;)Landroid/location/Location;");
		f_36 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "getProvider", "(Ljava/lang/String;)Landroid/location/LocationProvider;", "com/srt/appguard/monitor/MonitorInitializer", "m_36", "(Landroid/location/LocationManager;Ljava/lang/String;)Landroid/location/LocationProvider;");
		f_37 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "getProviders", "(Landroid/location/Criteria;Z)Ljava/util/List;", "com/srt/appguard/monitor/MonitorInitializer", "m_37", "(Landroid/location/LocationManager;Landroid/location/Criteria;Z)Ljava/util/List;");
		f_38 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "getProviders", "(Z)Ljava/util/List;", "com/srt/appguard/monitor/MonitorInitializer", "m_38", "(Landroid/location/LocationManager;Z)Ljava/util/List;");
		f_39 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "isProviderEnabled", "(Ljava/lang/String;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_39", "(Landroid/location/LocationManager;Ljava/lang/String;)Z");
		f_40 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "removeTestProvider", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_40", "(Landroid/location/LocationManager;Ljava/lang/String;)V");
		f_41 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestLocationUpdates", "(JFLandroid/location/Criteria;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_41", "(Landroid/location/LocationManager;JFLandroid/location/Criteria;Landroid/app/PendingIntent;)V");
		f_42 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestLocationUpdates", "(JFLandroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_42", "(Landroid/location/LocationManager;JFLandroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V");
		f_43 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestLocationUpdates", "(Ljava/lang/String;JFLandroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_43", "(Landroid/location/LocationManager;Ljava/lang/String;JFLandroid/app/PendingIntent;)V");
		f_44 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestLocationUpdates", "(Ljava/lang/String;JFLandroid/location/LocationListener;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_44", "(Landroid/location/LocationManager;Ljava/lang/String;JFLandroid/location/LocationListener;)V");
		f_45 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestLocationUpdates", "(Ljava/lang/String;JFLandroid/location/LocationListener;Landroid/os/Looper;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_45", "(Landroid/location/LocationManager;Ljava/lang/String;JFLandroid/location/LocationListener;Landroid/os/Looper;)V");
		f_46 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestSingleUpdate", "(Landroid/location/Criteria;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_46", "(Landroid/location/LocationManager;Landroid/location/Criteria;Landroid/app/PendingIntent;)V");
		f_47 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestSingleUpdate", "(Landroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_47", "(Landroid/location/LocationManager;Landroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V");
		f_48 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestSingleUpdate", "(Ljava/lang/String;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_48", "(Landroid/location/LocationManager;Ljava/lang/String;Landroid/app/PendingIntent;)V");
		f_49 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "requestSingleUpdate", "(Ljava/lang/String;Landroid/location/LocationListener;Landroid/os/Looper;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_49", "(Landroid/location/LocationManager;Ljava/lang/String;Landroid/location/LocationListener;Landroid/os/Looper;)V");
		f_50 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "sendExtraCommand", "(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_50", "(Landroid/location/LocationManager;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Z");
		f_51 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "setTestProviderEnabled", "(Ljava/lang/String;Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_51", "(Landroid/location/LocationManager;Ljava/lang/String;Z)V");
		f_52 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "setTestProviderLocation", "(Ljava/lang/String;Landroid/location/Location;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_52", "(Landroid/location/LocationManager;Ljava/lang/String;Landroid/location/Location;)V");
		f_53 = INSTRUMENTATION.hookMethod("android/location/LocationManager", "setTestProviderStatus", "(Ljava/lang/String;ILandroid/os/Bundle;J)V", "com/srt/appguard/monitor/MonitorInitializer", "m_53", "(Landroid/location/LocationManager;Ljava/lang/String;ILandroid/os/Bundle;J)V");
		f_54 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "isBluetoothA2dpOn", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_54", "(Landroid/media/AudioManager;)Z");
		f_55 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "isWiredHeadsetOn", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_55", "(Landroid/media/AudioManager;)Z");
		f_56 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setBluetoothScoOn", "(Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_56", "(Landroid/media/AudioManager;Z)V");
		f_57 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setMicrophoneMute", "(Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_57", "(Landroid/media/AudioManager;Z)V");
		f_58 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setMode", "(I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_58", "(Landroid/media/AudioManager;I)V");
		f_59 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setParameter", "(Ljava/lang/String;Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_59", "(Landroid/media/AudioManager;Ljava/lang/String;Ljava/lang/String;)V");
		f_60 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setParameters", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_60", "(Landroid/media/AudioManager;Ljava/lang/String;)V");
		f_61 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "setSpeakerphoneOn", "(Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_61", "(Landroid/media/AudioManager;Z)V");
		f_62 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "startBluetoothSco", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_62", "(Landroid/media/AudioManager;)V");
		f_63 = INSTRUMENTATION.hookMethod("android/media/AudioManager", "stopBluetoothSco", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_63", "(Landroid/media/AudioManager;)V");
		f_64 = INSTRUMENTATION.hookMethod("android/media/AudioRecord", "<init>", "(IIIII)V", "com/srt/appguard/monitor/MonitorInitializer", "m_64", "(Landroid/media/AudioRecord;IIIII)V");
		f_65 = INSTRUMENTATION.hookMethod("android/media/MediaPlayer", "setWakeMode", "(Landroid/content/Context;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_65", "(Landroid/media/MediaPlayer;Landroid/content/Context;I)V");
		f_66 = INSTRUMENTATION.hookMethod("android/media/MediaRecorder", "setAudioSource", "(I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_66", "(Landroid/media/MediaRecorder;I)V");
		f_67 = INSTRUMENTATION.hookMethod("android/media/MediaRecorder", "setVideoSource", "(I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_67", "(Landroid/media/MediaRecorder;I)V");
		f_68 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "requestRouteToHost", "(II)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_68", "(Ljava/lang/Object;II)Z");
		f_69 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setMobileDataEnabled", "(Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_69", "(Ljava/lang/Object;Z)V");
		f_70 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setNetworkPreference", "(I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_70", "(Ljava/lang/Object;I)V");
		f_71 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setRadio", "(ILjava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_71", "(Ljava/lang/Object;ILjava/lang/String;)I");
		f_72 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setRadio", "(IZ)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_72", "(Ljava/lang/Object;IZ)Z");
		f_73 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setRadios", "(ILjava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_73", "(Ljava/lang/Object;ILjava/lang/String;)I");
		f_74 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "setRadios", "(Z)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_74", "(Ljava/lang/Object;Z)Z");
		f_75 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "startUsingNetworkFeature", "(ILjava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_75", "(Ljava/lang/Object;ILjava/lang/String;)I");
		f_76 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "stopUsingNetworkFeature", "(ILjava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_76", "(Ljava/lang/Object;ILjava/lang/String;)I");
		f_77 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "tether", "(Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_77", "(Ljava/lang/Object;Ljava/lang/String;)I");
		f_78 = INSTRUMENTATION.hookMethod("android/net/ConnectivityManager", "untether", "(Ljava/lang/String;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_78", "(Ljava/lang/Object;Ljava/lang/String;)I");
		f_79 = INSTRUMENTATION.hookMethod("android/net/sip/SipAudioCall", "startAudio", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_79", "(Landroid/net/sip/SipAudioCall;)V");
		f_80 = INSTRUMENTATION.hookMethod("android/net/sip/SipAudioCall", "void setSpeakerMode", "(Z)V", "com/srt/appguard/monitor/MonitorInitializer", "m_80", "(Landroid/net/sip/SipAudioCall;Z)V");
		f_81 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager$MulticastLock", "acquire", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_81", "(Landroid/net/wifi/WifiManager$MulticastLock;)V");
		f_82 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager$MulticastLock", "release", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_82", "(Landroid/net/wifi/WifiManager$MulticastLock;)V");
		f_83 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager$WifiLock", "acquire", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_83", "(Landroid/net/wifi/WifiManager$WifiLock;)V");
		f_84 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager$WifiLock", "release", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_84", "(Landroid/net/wifi/WifiManager$WifiLock;)V");
		f_85 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "addNetwork", "(Landroid/net/wifi/WifiConfiguration;)I", "com/srt/appguard/monitor/MonitorInitializer", "m_85", "(Landroid/net/wifi/WifiManager;Landroid/net/wifi/WifiConfiguration;)I");
		f_86 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "disableNetwork", "(I)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_86", "(Landroid/net/wifi/WifiManager;I)Z");
		f_87 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "disconnect", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_87", "(Landroid/net/wifi/WifiManager;)Z");
		f_88 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "enableNetwork", "(IZ)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_88", "(Landroid/net/wifi/WifiManager;IZ)Z");
		f_89 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "initializeMulticastFiltering", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_89", "(Landroid/net/wifi/WifiManager;)Z");
		f_90 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "pingSupplicant", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_90", "(Landroid/net/wifi/WifiManager;)Z");
		f_91 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "reassociate", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_91", "(Landroid/net/wifi/WifiManager;)Z");
		f_92 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "reconnect", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_92", "(Landroid/net/wifi/WifiManager;)Z");
		f_93 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "removeNetwork", "(I)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_93", "(Landroid/net/wifi/WifiManager;I)Z");
		f_94 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "saveConfiguration", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_94", "(Landroid/net/wifi/WifiManager;)Z");
		f_95 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "setWifiApEnabled", "(Landroid/net/wifi/WifiConfiguration;Z)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_95", "(Landroid/net/wifi/WifiManager;Landroid/net/wifi/WifiConfiguration;Z)Z");
		f_96 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "setWifiEnabled", "(Z)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_96", "(Landroid/net/wifi/WifiManager;Z)Z");
		f_97 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "startScan", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_97", "(Landroid/net/wifi/WifiManager;)Z");
		f_98 = INSTRUMENTATION.hookMethod("android/net/wifi/WifiManager", "startScanActive", "()Z", "com/srt/appguard/monitor/MonitorInitializer", "m_98", "(Landroid/net/wifi/WifiManager;)Z");
		f_99 = INSTRUMENTATION.hookMethod("android/os/PowerManager$WakeLock", "acquire", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_99", "(Landroid/os/PowerManager$WakeLock;)V");
		f_100 = INSTRUMENTATION.hookMethod("android/os/PowerManager$WakeLock", "acquire", "(J)V", "com/srt/appguard/monitor/MonitorInitializer", "m_100", "(Landroid/os/PowerManager$WakeLock;J)V");
		f_101 = INSTRUMENTATION.hookMethod("android/os/PowerManager$WakeLock", "release", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_101", "(Landroid/os/PowerManager$WakeLock;)V");
		f_102 = INSTRUMENTATION.hookMethod("android/os/PowerManager$WakeLock", "release", "(I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_102", "(Landroid/os/PowerManager$WakeLock;I)V");
		f_103 = INSTRUMENTATION.hookMethod("android/server/BluetoothA2dpService", "resumeSink", "(Landroid/bluetooth/BluetoothDevice;)Z", "com/srt/appguard/monitor/MonitorInitializer", "m_103", "(Ljava/lang/Object;Landroid/bluetooth/BluetoothDevice;)Z");
		f_104 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "cancel", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_104", "(Landroid/speech/SpeechRecognizer;)V");
		f_105 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "handleCancelMessage", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_105", "(Landroid/speech/SpeechRecognizer;)V");
		f_106 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "handleStartListening", "(Landroid/content/Intent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_106", "(Landroid/speech/SpeechRecognizer;Landroid/content/Intent;)V");
		f_107 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "handleStopMessage", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_107", "(Landroid/speech/SpeechRecognizer;)V");
		f_108 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "startListening", "(Landroid/content/Intent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_108", "(Landroid/speech/SpeechRecognizer;Landroid/content/Intent;)V");
		f_109 = INSTRUMENTATION.hookMethod("android/speech/SpeechRecognizer", "stopListening", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_109", "(Landroid/speech/SpeechRecognizer;)V");
		f_110 = INSTRUMENTATION.hookMethod("android/telephony/SmsManager", "sendDataMessage", "(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_110", "(Landroid/telephony/SmsManager;Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		f_111 = INSTRUMENTATION.hookMethod("android/telephony/SmsManager", "sendMultipartTextMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_111", "(Landroid/telephony/SmsManager;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V");
		f_112 = INSTRUMENTATION.hookMethod("android/telephony/SmsManager", "sendTextMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_112", "(Landroid/telephony/SmsManager;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		f_113 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getCellLocation", "()Landroid/telephony/CellLocation;", "com/srt/appguard/monitor/MonitorInitializer", "m_113", "(Landroid/telephony/TelephonyManager;)Landroid/telephony/CellLocation;");
		f_114 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getDeviceId", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_114", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_115 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getDeviceSoftwareVersion", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_115", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_116 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getLine1Number", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_116", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_117 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getNeighboringCellInfo", "()Ljava/util/List;", "com/srt/appguard/monitor/MonitorInitializer", "m_117", "(Landroid/telephony/TelephonyManager;)Ljava/util/List;");
		f_118 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getSimSerialNumber", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_118", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_119 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getSubscriberId", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_119", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_120 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getVoiceMailAlphaTag", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_120", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_121 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "getVoiceMailNumber", "()Ljava/lang/String;", "com/srt/appguard/monitor/MonitorInitializer", "m_121", "(Landroid/telephony/TelephonyManager;)Ljava/lang/String;");
		f_122 = INSTRUMENTATION.hookMethod("android/telephony/TelephonyManager", "listen", "(Landroid/telephony/PhoneStateListener;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_122", "(Landroid/telephony/TelephonyManager;Landroid/telephony/PhoneStateListener;I)V");
		f_123 = INSTRUMENTATION.hookMethod("android/telephony/gsm/SmsManager", "sendDataMessage", "(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_123", "(Landroid/telephony/gsm/SmsManager;Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		f_124 = INSTRUMENTATION.hookMethod("android/telephony/gsm/SmsManager", "sendMultipartTextMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_124", "(Landroid/telephony/gsm/SmsManager;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V");
		f_125 = INSTRUMENTATION.hookMethod("android/telephony/gsm/SmsManager", "sendTextMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_125", "(Landroid/telephony/gsm/SmsManager;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		f_126 = INSTRUMENTATION.hookMethod("android/webkit/WebView", "loadDataWithBaseURL", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_126", "(Landroid/webkit/WebView;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
		f_127 = INSTRUMENTATION.hookMethod("android/webkit/WebView", "loadUrl", "(Ljava/lang/String;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_127", "(Landroid/webkit/WebView;Ljava/lang/String;)V");
		f_128 = INSTRUMENTATION.hookMethod("android/webkit/WebView", "loadUrl", "(Ljava/lang/String;Ljava/util/Map;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_128", "(Landroid/webkit/WebView;Ljava/lang/String;Ljava/util/Map;)V");
		f_129 = INSTRUMENTATION.hookMethod("java/net/DatagramSocket", "connect", "(Ljava/net/InetAddress;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_129", "(Ljava/net/DatagramSocket;Ljava/net/InetAddress;I)V");
		f_130 = INSTRUMENTATION.hookMethod("java/net/DatagramSocket", "connect", "(Ljava/net/SocketAddress;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_130", "(Ljava/net/DatagramSocket;Ljava/net/SocketAddress;)V");
		f_131 = INSTRUMENTATION.hookMethod("java/net/HttpURLConnection", "connect", "()V", "com/srt/appguard/monitor/MonitorInitializer", "m_131", "(Ljava/net/HttpURLConnection;)V");
		f_132 = INSTRUMENTATION.hookMethod("java/net/MulticastSocket", "joinGroup", "(Ljava/net/InetAddress;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_132", "(Ljava/net/MulticastSocket;Ljava/net/InetAddress;)V");
		f_133 = INSTRUMENTATION.hookMethod("java/net/MulticastSocket", "joinGroup", "(Ljava/net/SocketAddress;Ljava/net/NetworkInterface;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_133", "(Ljava/net/MulticastSocket;Ljava/net/SocketAddress;Ljava/net/NetworkInterface;)V");
		f_134 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/lang/String;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_134", "(Ljava/net/Socket;Ljava/lang/String;I)V");
		f_135 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/lang/String;ILjava/net/InetAddress;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_135", "(Ljava/net/Socket;Ljava/lang/String;ILjava/net/InetAddress;I)V");
		f_136 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/lang/String;IZ)V", "com/srt/appguard/monitor/MonitorInitializer", "m_136", "(Ljava/net/Socket;Ljava/lang/String;IZ)V");
		f_137 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/net/InetAddress;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_137", "(Ljava/net/Socket;Ljava/net/InetAddress;I)V");
		f_138 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/net/InetAddress;ILjava/net/InetAddress;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_138", "(Ljava/net/Socket;Ljava/net/InetAddress;ILjava/net/InetAddress;I)V");
		f_139 = INSTRUMENTATION.hookMethod("java/net/Socket", "<init>", "(Ljava/net/InetAddress;IZ)V", "com/srt/appguard/monitor/MonitorInitializer", "m_139", "(Ljava/net/Socket;Ljava/net/InetAddress;IZ)V");
		f_140 = INSTRUMENTATION.hookMethod("java/net/Socket", "connect", "(Ljava/net/SocketAddress;)V", "com/srt/appguard/monitor/MonitorInitializer", "m_140", "(Ljava/net/Socket;Ljava/net/SocketAddress;)V");
		f_141 = INSTRUMENTATION.hookMethod("java/net/Socket", "connect", "(Ljava/net/SocketAddress;I)V", "com/srt/appguard/monitor/MonitorInitializer", "m_141", "(Ljava/net/Socket;Ljava/net/SocketAddress;I)V");
		f_142 = INSTRUMENTATION.hookMethod("java/net/URL", "openConnection", "()Ljava/net/URLConnection;", "com/srt/appguard/monitor/MonitorInitializer", "m_142", "(Ljava/net/URL;)Ljava/net/URLConnection;");
		f_143 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)Lorg/apache/http/HttpResponse;", "com/srt/appguard/monitor/MonitorInitializer", "m_143", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)Lorg/apache/http/HttpResponse;");
		f_144 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;", "com/srt/appguard/monitor/MonitorInitializer", "m_144", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;");
		f_145 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;", "com/srt/appguard/monitor/MonitorInitializer", "m_145", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;");
		f_146 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;", "com/srt/appguard/monitor/MonitorInitializer", "m_146", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;");
		f_147 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;", "com/srt/appguard/monitor/MonitorInitializer", "m_147", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;");
		f_148 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;", "com/srt/appguard/monitor/MonitorInitializer", "m_148", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;");
		f_149 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;", "com/srt/appguard/monitor/MonitorInitializer", "m_149", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;");
        f_150 = INSTRUMENTATION.hookMethod("org/apache/http/impl/client/DefaultHttpClient", "execute", "(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;", "com/srt/appguard/monitor/MonitorInitializer", "m_150", "(Lorg/apache/http/impl/client/DefaultHttpClient;Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;");
	}

	// Landroid/app/Activity;->onResume()V
	public static void m_0(android.app.Activity _this) throws Throwable {
		try {
			RefreshConfigPolicy.instance.Activity_onResume(_this);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_0, _this);
	}

	// Landroid/app/Activity;->startActivityForResult(Landroid/content/Intent;I)V
	public static void m_1(android.app.Activity _this, android.content.Intent arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.startActivity(_this, arg0);
			CallPhonePolicy.instance.startActivity(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_1, _this, arg0, arg1);
	}

	// Landroid/app/Activity;->startActivityFromChild(Landroid/app/Activity;Landroid/content/Intent;I)V
	public static void m_2(android.app.Activity _this, android.app.Activity arg0, android.content.Intent arg1, int arg2) throws Throwable {
		try {
			InternetPolicy.instance.startActivity(_this, arg0, arg1);
			CallPhonePolicy.instance.startActivity(_this, arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_2, _this, arg0, arg1, arg2);
	}

	// Landroid/app/Activity;->startActivityIfNeeded(Landroid/content/Intent;I)Z
	public static boolean m_3(android.app.Activity _this, android.content.Intent arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.startActivity(_this, arg0);
			CallPhonePolicy.instance.startActivity(_this, arg0);
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_3, _this, arg0, arg1);
	}

	// Landroid/app/ActivityManager;->getRecentTasks(II)Ljava/util/List;
	public static java.util.List m_4(android.app.ActivityManager _this, int arg0, int arg1) throws Throwable {
		try {
			GetTasksPolicy.instance.getTasks_$catchAll();
		} catch (MonitorException e) {
			return (java.util.List) e.getValue();
		}
		return (java.util.List) INSTRUMENTATION.callReplacedObjectMethod(f_4, _this, arg0, arg1);
	}

	// Landroid/app/ActivityManager;->getRunningTasks(I)Ljava/util/List;
	public static java.util.List m_5(android.app.ActivityManager _this, int arg0) throws Throwable {
		try {
			GetTasksPolicy.instance.getTasks_$catchAll();
		} catch (MonitorException e) {
			return (java.util.List) e.getValue();
		}
		return (java.util.List) INSTRUMENTATION.callReplacedObjectMethod(f_5, _this, arg0);
	}

	// Landroid/app/ActivityManagerNative;->getRecentTasks(II)Ljava/util/List
	public static Object m_6(Object _this, int arg0, int arg1) throws Throwable {
		try {
			GetTasksPolicy.instance.getTasks_$catchAll();
		} catch (MonitorException e) {
			return (Object) e.getValue();
		}
		return (Object) INSTRUMENTATION.callReplacedObjectMethod(f_6, _this, arg0, arg1);
	}

	// Landroid/bluetooth/BluetoothHeadset;->startVoiceRecognition(Landroid/bluetooth/BluetoothDevice;)Z
	public static boolean m_7(android.bluetooth.BluetoothHeadset _this, android.bluetooth.BluetoothDevice arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_7, _this, arg0);
	}

	// Landroid/bluetooth/BluetoothHeadset;->stopVoiceRecognition(Landroid/bluetooth/BluetoothDevice;)Z
	public static boolean m_8(android.bluetooth.BluetoothHeadset _this, android.bluetooth.BluetoothDevice arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_8, _this, arg0);
	}

	// Landroid/bluetooth/HeadsetBase;->acquireWakeLock()V
	public static void m_9(Object _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_9, _this);
	}

	// Landroid/bluetooth/HeadsetBase;->releaseWakeLock()V
	public static void m_10(Object _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_10, _this);
	}

	// Landroid/content/ContentProviderClient;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I
	public static int m_11(android.content.ContentProviderClient _this, android.net.Uri arg0, android.content.ContentValues[] arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_11, _this, arg0, arg1);
	}

	// Landroid/content/ContentProviderClient;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
	public static int m_12(android.content.ContentProviderClient _this, android.net.Uri arg0, String arg1, String[] arg2) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__delete(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_12, _this, arg0, arg1, arg2);
	}

	// Landroid/content/ContentProviderClient;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
	public static android.net.Uri m_13(android.content.ContentProviderClient _this, android.net.Uri arg0, android.content.ContentValues arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__insert(_this, arg0);
		} catch (MonitorException e) {
			return (android.net.Uri) e.getValue();
		}
		return (android.net.Uri) INSTRUMENTATION.callReplacedObjectMethod(f_13, _this, arg0, arg1);
	}

	// Landroid/content/ContentProviderClient;->openFile(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
	public static android.os.ParcelFileDescriptor m_14(android.content.ContentProviderClient _this, android.net.Uri arg0, String arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
			CalendarPolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
			BrowserPolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
			SettingsPolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
			SmsPolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
			MediaStorePolicy.instance.android_content_ContentProviderClient__openFile(_this, arg0, arg1);
		} catch (MonitorException e) {
			return (android.os.ParcelFileDescriptor) e.getValue();
		}
		return (android.os.ParcelFileDescriptor) INSTRUMENTATION.callReplacedObjectMethod(f_14, _this, arg0, arg1);
	}

	// Landroid/content/ContentProviderClient;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
	public static android.database.Cursor m_15(android.content.ContentProviderClient _this, android.net.Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			CalendarPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			BrowserPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			SettingsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			SmsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			MediaStorePolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
		} catch (MonitorException e) {
			return (android.database.Cursor) e.getValue();
		}
		return (android.database.Cursor) INSTRUMENTATION.callReplacedObjectMethod(f_15, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/content/ContentProviderClient;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
	public static int m_16(android.content.ContentProviderClient _this, android.net.Uri arg0, android.content.ContentValues arg1, String arg2, String[] arg3) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__update(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_16, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/content/ContentResolver;->bulkInsert(Landroid/net/Uri;[Landroid/content/ContentValues;)I
	public static int m_17(android.content.ContentResolver _this, android.net.Uri arg0, android.content.ContentValues[] arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__bulkInsert(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_17, _this, arg0, arg1);
	}

	// Landroid/content/ContentResolver;->delete(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
	public static int m_18(android.content.ContentResolver _this, android.net.Uri arg0, String arg1, String[] arg2) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__delete(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__delete(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_18, _this, arg0, arg1, arg2);
	}

	// Landroid/content/ContentResolver;->insert(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
	public static android.net.Uri m_19(android.content.ContentResolver _this, android.net.Uri arg0, android.content.ContentValues arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__insert(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__insert(_this, arg0);
		} catch (MonitorException e) {
			return (android.net.Uri) e.getValue();
		}
		return (android.net.Uri) INSTRUMENTATION.callReplacedObjectMethod(f_19, _this, arg0, arg1);
	}

	// Landroid/content/ContentResolver;->openFileDescriptor(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
	public static android.os.ParcelFileDescriptor m_20(android.content.ContentResolver _this, android.net.Uri arg0, String arg1) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
			CalendarPolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
			BrowserPolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
			SettingsPolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
			SmsPolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
			MediaStorePolicy.instance.android_content_ContentResolver__openFileDescriptor(_this, arg0, arg1);
		} catch (MonitorException e) {
			return (android.os.ParcelFileDescriptor) e.getValue();
		}
		return (android.os.ParcelFileDescriptor) INSTRUMENTATION.callReplacedObjectMethod(f_20, _this, arg0, arg1);
	}

	// Landroid/content/ContentResolver;->openInputStream(Landroid/net/Uri;)Ljava/io/InputStream;
	public static java.io.InputStream m_21(android.content.ContentResolver _this, android.net.Uri arg0) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__openInputStream(_this, arg0);
		} catch (MonitorException e) {
			return (java.io.InputStream) e.getValue();
		}
		return (java.io.InputStream) INSTRUMENTATION.callReplacedObjectMethod(f_21, _this, arg0);
	}

	// Landroid/content/ContentResolver;->query(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
	public static android.database.Cursor m_22(android.content.ContentResolver _this, android.net.Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			CalendarPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			BrowserPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			SettingsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			SmsPolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
			MediaStorePolicy.instance.android_content_ContentResolver__query(_this, arg0, arg1);
		} catch (MonitorException e) {
			return (android.database.Cursor) e.getValue();
		}
		return (android.database.Cursor) INSTRUMENTATION.callReplacedObjectMethod(f_22, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/content/ContentResolver;->registerContentObserver(Landroid/net/Uri;ZLandroid/database/ContentObserver;)V
	public static void m_23(android.content.ContentResolver _this, android.net.Uri arg0, boolean arg1, android.database.ContentObserver arg2) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__registerContentObserver(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_23, _this, arg0, arg1, arg2);
	}

	// Landroid/content/ContentResolver;->update(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
	public static int m_24(android.content.ContentResolver _this, android.net.Uri arg0, android.content.ContentValues arg1, String arg2, String[] arg3) throws Throwable {
		try {
			ContactsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			CalendarPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			BrowserPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			SettingsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			SmsPolicy.instance.android_content_ContentResolver__update(_this, arg0);
			MediaStorePolicy.instance.android_content_ContentResolver__update(_this, arg0);
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_24, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/hardware/Camera;->open()Landroid/hardware/Camera;
	public static android.hardware.Camera m_25() throws Throwable {
		try {
			CameraPolicy.instance.android_hardware_Camera__open();
		} catch (MonitorException e) {
			return (android.hardware.Camera) e.getValue();
		}
		return (android.hardware.Camera) INSTRUMENTATION.callReplacedStaticObjectMethod(f_25, android.hardware.Camera.class);
	}

	// Landroid/hardware/Camera;->open(I)Landroid/hardware/Camera;
	public static android.hardware.Camera m_26(int arg0) throws Throwable {
		try {
			CameraPolicy.instance.android_hardware_Camera__open();
		} catch (MonitorException e) {
			return (android.hardware.Camera) e.getValue();
		}
		return (android.hardware.Camera) INSTRUMENTATION.callReplacedStaticObjectMethod(f_26, android.hardware.Camera.class, arg0);
	}

	// Landroid/location/LocationManager;->addGpsStatusListener(Landroid/location/GpsStatus$Listener;)Z
	public static boolean m_27(android.location.LocationManager _this, android.location.GpsStatus.Listener arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__addGpsStatusListener();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_27, _this, arg0);
	}

	// Landroid/location/LocationManager;->addNmeaListener(Landroid/location/GpsStatus$NmeaListener;)Z
	public static boolean m_28(android.location.LocationManager _this, android.location.GpsStatus.NmeaListener arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__addNmeaListener();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_28, _this, arg0);
	}

	// Landroid/location/LocationManager;->addProximityAlert(DDFJLandroid/app/PendingIntent;)V
	public static void m_29(android.location.LocationManager _this, double arg0, double arg1, float arg2, long arg3, android.app.PendingIntent arg4) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__addProximityAlert();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_29, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/location/LocationManager;->addTestProvider(Ljava/lang/String;ZZZZZZZII)V
	public static void m_30(android.location.LocationManager _this, String arg0, boolean arg1, boolean arg2, boolean arg3, boolean arg4, boolean arg5, boolean arg6, boolean arg7, int arg8, int arg9) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_30, _this, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}

	// Landroid/location/LocationManager;->clearTestProviderEnabled(Ljava/lang/String;)V
	public static void m_31(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_31, _this, arg0);
	}

	// Landroid/location/LocationManager;->clearTestProviderLocation(Ljava/lang/String;)V
	public static void m_32(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_32, _this, arg0);
	}

	// Landroid/location/LocationManager;->clearTestProviderStatus(Ljava/lang/String;)V
	public static void m_33(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_33, _this, arg0);
	}

	// Landroid/location/LocationManager;->getBestProvider(Landroid/location/Criteria;Z)Ljava/lang/String;
	public static String m_34(android.location.LocationManager _this, android.location.Criteria arg0, boolean arg1) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__getBestProvider();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_34, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->getLastKnownLocation(Ljava/lang/String;)Landroid/location/Location;
	public static android.location.Location m_35(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__getLastKnownLocation();
		} catch (MonitorException e) {
			return (android.location.Location) e.getValue();
		}
		return (android.location.Location) INSTRUMENTATION.callReplacedObjectMethod(f_35, _this, arg0);
	}

	// Landroid/location/LocationManager;->getProvider(Ljava/lang/String;)Landroid/location/LocationProvider;
	public static android.location.LocationProvider m_36(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__getProvider();
		} catch (MonitorException e) {
			return (android.location.LocationProvider) e.getValue();
		}
		return (android.location.LocationProvider) INSTRUMENTATION.callReplacedObjectMethod(f_36, _this, arg0);
	}

	// Landroid/location/LocationManager;->getProviders(Landroid/location/Criteria;Z)Ljava/util/List;
	public static java.util.List m_37(android.location.LocationManager _this, android.location.Criteria arg0, boolean arg1) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__getProviders();
		} catch (MonitorException e) {
			return (java.util.List) e.getValue();
		}
		return (java.util.List) INSTRUMENTATION.callReplacedObjectMethod(f_37, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->getProviders(Z)Ljava/util/List;
	public static java.util.List m_38(android.location.LocationManager _this, boolean arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__getProviders();
		} catch (MonitorException e) {
			return (java.util.List) e.getValue();
		}
		return (java.util.List) INSTRUMENTATION.callReplacedObjectMethod(f_38, _this, arg0);
	}

	// Landroid/location/LocationManager;->isProviderEnabled(Ljava/lang/String;)Z
	public static boolean m_39(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__isProviderEnabled();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_39, _this, arg0);
	}

	// Landroid/location/LocationManager;->removeTestProvider(Ljava/lang/String;)V
	public static void m_40(android.location.LocationManager _this, String arg0) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_40, _this, arg0);
	}

	// Landroid/location/LocationManager;->requestLocationUpdates(JFLandroid/location/Criteria;Landroid/app/PendingIntent;)V
	public static void m_41(android.location.LocationManager _this, long arg0, float arg1, android.location.Criteria arg2, android.app.PendingIntent arg3) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_41, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/location/LocationManager;->requestLocationUpdates(JFLandroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V
	public static void m_42(android.location.LocationManager _this, long arg0, float arg1, android.location.Criteria arg2, android.location.LocationListener arg3, android.os.Looper arg4) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_42, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/app/PendingIntent;)V
	public static void m_43(android.location.LocationManager _this, String arg0, long arg1, float arg2, android.app.PendingIntent arg3) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_43, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/location/LocationListener;)V
	public static void m_44(android.location.LocationManager _this, String arg0, long arg1, float arg2, android.location.LocationListener arg3) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_44, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/location/LocationManager;->requestLocationUpdates(Ljava/lang/String;JFLandroid/location/LocationListener;Landroid/os/Looper;)V
	public static void m_45(android.location.LocationManager _this, String arg0, long arg1, float arg2, android.location.LocationListener arg3, android.os.Looper arg4) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_45, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/location/LocationManager;->requestSingleUpdate(Landroid/location/Criteria;Landroid/app/PendingIntent;)V
	public static void m_46(android.location.LocationManager _this, android.location.Criteria arg0, android.app.PendingIntent arg1) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_46, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->requestSingleUpdate(Landroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V
	public static void m_47(android.location.LocationManager _this, android.location.Criteria arg0, android.location.LocationListener arg1, android.os.Looper arg2) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_47, _this, arg0, arg1, arg2);
	}

	// Landroid/location/LocationManager;->requestSingleUpdate(Ljava/lang/String;Landroid/app/PendingIntent;)V
	public static void m_48(android.location.LocationManager _this, String arg0, android.app.PendingIntent arg1) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_48, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->requestSingleUpdate(Ljava/lang/String;Landroid/location/LocationListener;Landroid/os/Looper;)V
	public static void m_49(android.location.LocationManager _this, String arg0, android.location.LocationListener arg1, android.os.Looper arg2) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__requestLocationUpdates();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_49, _this, arg0, arg1, arg2);
	}

	// Landroid/location/LocationManager;->sendExtraCommand(Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Z
	public static boolean m_50(android.location.LocationManager _this, String arg0, String arg1, android.os.Bundle arg2) throws Throwable {
		try {
			LocationPolicy.instance.android_location_LocationManager__sendExtraCommand();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_50, _this, arg0, arg1, arg2);
	}

	// Landroid/location/LocationManager;->setTestProviderEnabled(Ljava/lang/String;Z)V
	public static void m_51(android.location.LocationManager _this, String arg0, boolean arg1) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_51, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->setTestProviderLocation(Ljava/lang/String;Landroid/location/Location;)V
	public static void m_52(android.location.LocationManager _this, String arg0, android.location.Location arg1) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_52, _this, arg0, arg1);
	}

	// Landroid/location/LocationManager;->setTestProviderStatus(Ljava/lang/String;ILandroid/os/Bundle;J)V
	public static void m_53(android.location.LocationManager _this, String arg0, int arg1, android.os.Bundle arg2, long arg3) throws Throwable {
		try {
			MockLocationPolicy.instance.mockLocation__$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_53, _this, arg0, arg1, arg2, arg3);
	}

	// Landroid/media/AudioManager;->isBluetoothA2dpOn()Z
	public static boolean m_54(android.media.AudioManager _this) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_54, _this);
	}

	// Landroid/media/AudioManager;->isWiredHeadsetOn()Z
	public static boolean m_55(android.media.AudioManager _this) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_55, _this);
	}

	// Landroid/media/AudioManager;->setBluetoothScoOn(Z)V
	public static void m_56(android.media.AudioManager _this, boolean arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_56, _this, arg0);
	}

	// Landroid/media/AudioManager;->setMicrophoneMute(Z)V
	public static void m_57(android.media.AudioManager _this, boolean arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_57, _this, arg0);
	}

	// Landroid/media/AudioManager;->setMode(I)V
	public static void m_58(android.media.AudioManager _this, int arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_58, _this, arg0);
	}

	// Landroid/media/AudioManager;->setParameter(Ljava/lang/String;Ljava/lang/String;)V
	public static void m_59(android.media.AudioManager _this, String arg0, String arg1) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_59, _this, arg0, arg1);
	}

	// Landroid/media/AudioManager;->setParameters(Ljava/lang/String;)V
	public static void m_60(android.media.AudioManager _this, String arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_60, _this, arg0);
	}

	// Landroid/media/AudioManager;->setSpeakerphoneOn(Z)V
	public static void m_61(android.media.AudioManager _this, boolean arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_61, _this, arg0);
	}

	// Landroid/media/AudioManager;->startBluetoothSco()V
	public static void m_62(android.media.AudioManager _this) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_62, _this);
	}

	// Landroid/media/AudioManager;->stopBluetoothSco()V
	public static void m_63(android.media.AudioManager _this) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_63, _this);
	}

	// Landroid/media/AudioRecord;-><init>(IIIII)V
	public static void m_64(android.media.AudioRecord _this, int arg0, int arg1, int arg2, int arg3, int arg4) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_media_AudioRecord_$init();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_64, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/media/MediaPlayer;->setWakeMode(Landroid/content/Context;I)V
	public static void m_65(android.media.MediaPlayer _this, android.content.Context arg0, int arg1) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_65, _this, arg0, arg1);
	}

	// Landroid/media/MediaRecorder;->setAudioSource(I)V
	public static void m_66(android.media.MediaRecorder _this, int arg0) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_media_MediaRecorder__setAudioSource();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_66, _this, arg0);
	}

	// Landroid/media/MediaRecorder;->setVideoSource(I)V
	public static void m_67(android.media.MediaRecorder _this, int arg0) throws Throwable {
		try {
			CameraPolicy.instance.android_media_MediaRecorder__setVideoSource(_this);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_67, _this, arg0);
	}

	// Landroid/net/ConnectivityManager;->requestRouteToHost(II)Z
	public static boolean m_68(Object _this, int arg0, int arg1) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_68, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->setMobileDataEnabled(Z)V
	public static void m_69(Object _this, boolean arg0) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_69, _this, arg0);
	}

	// Landroid/net/ConnectivityManager;->setNetworkPreference(I)V
	public static void m_70(Object _this, int arg0) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_70, _this, arg0);
	}

	// Landroid/net/ConnectivityManager;->setRadio(ILjava/lang/String;)I
	public static int m_71(Object _this, int arg0, String arg1) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_ConnectivityManager_$catchAll_I();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_71, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->setRadio(IZ)Z
	public static boolean m_72(Object _this, int arg0, boolean arg1) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$catchAll_Z();
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_72, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->setRadios(ILjava/lang/String;)I
	public static int m_73(Object _this, int arg0, String arg1) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_ConnectivityManager_$catchAll_I();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_73, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->setRadios(Z)Z
	public static boolean m_74(Object _this, boolean arg0) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$catchAll_Z();
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_74, _this, arg0);
	}

	// Landroid/net/ConnectivityManager;->startUsingNetworkFeature(ILjava/lang/String;)I
	public static int m_75(Object _this, int arg0, String arg1) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$networkFeature();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_75, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->stopUsingNetworkFeature(ILjava/lang/String;)I
	public static int m_76(Object _this, int arg0, String arg1) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$networkFeature();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_76, _this, arg0, arg1);
	}

	// Landroid/net/ConnectivityManager;->tether(Ljava/lang/String;)I
	public static int m_77(Object _this, String arg0) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$tether();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_77, _this, arg0);
	}

	// Landroid/net/ConnectivityManager;->untether(Ljava/lang/String;)I
	public static int m_78(Object _this, String arg0) throws Throwable {
		try {
			ChangeNetworkStatePolicy.instance.android_net_ConnectivityManager_$tether();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_78, _this, arg0);
	}

	// Landroid/net/sip/SipAudioCall;->startAudio()V
	public static void m_79(android.net.sip.SipAudioCall _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_79, _this);
	}

	// Landroid/net/sip/SipAudioCall;->void setSpeakerMode(Z)V
	public static void m_80(android.net.sip.SipAudioCall _this, boolean arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_V();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_80, _this, arg0);
	}

	// Landroid/net/wifi/WifiManager$MulticastLock;->acquire()V
	public static void m_81(android.net.wifi.WifiManager.MulticastLock _this) throws Throwable {
		try {
			ChangeWifiMulticastStatePolicy.instance.android_net_WifiManager_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_81, _this);
	}

	// Landroid/net/wifi/WifiManager$MulticastLock;->release()V
	public static void m_82(android.net.wifi.WifiManager.MulticastLock _this) throws Throwable {
		try {
			ChangeWifiMulticastStatePolicy.instance.android_net_WifiManager_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_82, _this);
	}

	// Landroid/net/wifi/WifiManager$WifiLock;->acquire()V
	public static void m_83(android.net.wifi.WifiManager.WifiLock _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_83, _this);
	}

	// Landroid/net/wifi/WifiManager$WifiLock;->release()V
	public static void m_84(android.net.wifi.WifiManager.WifiLock _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_84, _this);
	}

	// Landroid/net/wifi/WifiManager;->addNetwork(Landroid/net/wifi/WifiConfiguration;)I
	public static int m_85(android.net.wifi.WifiManager _this, android.net.wifi.WifiConfiguration arg0) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$addNetwork();
		} catch (MonitorException e) {
			return e.getIntValue();
		}
		return INSTRUMENTATION.callReplacedIntMethod(f_85, _this, arg0);
	}

	// Landroid/net/wifi/WifiManager;->disableNetwork(I)Z
	public static boolean m_86(android.net.wifi.WifiManager _this, int arg0) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_86, _this, arg0);
	}

	// Landroid/net/wifi/WifiManager;->disconnect()Z
	public static boolean m_87(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_87, _this);
	}

	// Landroid/net/wifi/WifiManager;->enableNetwork(IZ)Z
	public static boolean m_88(android.net.wifi.WifiManager _this, int arg0, boolean arg1) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_88, _this, arg0, arg1);
	}

	// Landroid/net/wifi/WifiManager;->initializeMulticastFiltering()Z
	public static boolean m_89(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiMulticastStatePolicy.instance.android_net_WifiManager_$catchAll();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_89, _this);
	}

	// Landroid/net/wifi/WifiManager;->pingSupplicant()Z
	public static boolean m_90(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_90, _this);
	}

	// Landroid/net/wifi/WifiManager;->reassociate()Z
	public static boolean m_91(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_91, _this);
	}

	// Landroid/net/wifi/WifiManager;->reconnect()Z
	public static boolean m_92(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_92, _this);
	}

	// Landroid/net/wifi/WifiManager;->removeNetwork(I)Z
	public static boolean m_93(android.net.wifi.WifiManager _this, int arg0) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_93, _this, arg0);
	}

	// Landroid/net/wifi/WifiManager;->saveConfiguration()Z
	public static boolean m_94(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_94, _this);
	}

	// Landroid/net/wifi/WifiManager;->setWifiApEnabled(Landroid/net/wifi/WifiConfiguration;Z)Z
	public static boolean m_95(android.net.wifi.WifiManager _this, android.net.wifi.WifiConfiguration arg0, boolean arg1) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_95, _this, arg0, arg1);
	}

	// Landroid/net/wifi/WifiManager;->setWifiEnabled(Z)Z
	public static boolean m_96(android.net.wifi.WifiManager _this, boolean arg0) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_96, _this, arg0);
	}

	// Landroid/net/wifi/WifiManager;->startScan()Z
	public static boolean m_97(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_97, _this);
	}

	// Landroid/net/wifi/WifiManager;->startScanActive()Z
	public static boolean m_98(android.net.wifi.WifiManager _this) throws Throwable {
		try {
			ChangeWifiStatePolicy.instance.android_net_WifiManager_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_98, _this);
	}

	// Landroid/os/PowerManager$WakeLock;->acquire()V
	public static void m_99(android.os.PowerManager.WakeLock _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_99, _this);
	}

	// Landroid/os/PowerManager$WakeLock;->acquire(J)V
	public static void m_100(android.os.PowerManager.WakeLock _this, long arg0) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_100, _this, arg0);
	}

	// Landroid/os/PowerManager$WakeLock;->release()V
	public static void m_101(android.os.PowerManager.WakeLock _this) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_101, _this);
	}

	// Landroid/os/PowerManager$WakeLock;->release(I)V
	public static void m_102(android.os.PowerManager.WakeLock _this, int arg0) throws Throwable {
		try {
			WakeLockPolicy.instance.wakeLock_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_102, _this, arg0);
	}

	// Landroid/server/BluetoothA2dpService;->resumeSink(Landroid/bluetooth/BluetoothDevice;)Z
	public static boolean m_103(Object _this, android.bluetooth.BluetoothDevice arg0) throws Throwable {
		try {
			ModifyAudioSettingsPolicy.instance.modifyAudioSettings_$catchAll_Z();
		} catch (MonitorException e) {
			return e.getBooleanValue();
		}
		return INSTRUMENTATION.callReplacedBooleanMethod(f_103, _this, arg0);
	}

	// Landroid/speech/SpeechRecognizer;->cancel()V
	public static void m_104(android.speech.SpeechRecognizer _this) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_104, _this);
	}

	// Landroid/speech/SpeechRecognizer;->handleCancelMessage()V
	public static void m_105(android.speech.SpeechRecognizer _this) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_105, _this);
	}

	// Landroid/speech/SpeechRecognizer;->handleStartListening(Landroid/content/Intent;)V
	public static void m_106(android.speech.SpeechRecognizer _this, android.content.Intent arg0) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_106, _this, arg0);
	}

	// Landroid/speech/SpeechRecognizer;->handleStopMessage()V
	public static void m_107(android.speech.SpeechRecognizer _this) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_107, _this);
	}

	// Landroid/speech/SpeechRecognizer;->startListening(Landroid/content/Intent;)V
	public static void m_108(android.speech.SpeechRecognizer _this, android.content.Intent arg0) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_108, _this, arg0);
	}

	// Landroid/speech/SpeechRecognizer;->stopListening()V
	public static void m_109(android.speech.SpeechRecognizer _this) throws Throwable {
		try {
			RecordAudioPolicy.instance.android_speech_SpeechRecognizer_$catchAll();
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_109, _this);
	}

	// Landroid/telephony/SmsManager;->sendDataMessage(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V
	public static void m_110(android.telephony.SmsManager _this, String arg0, String arg1, short arg2, byte[] arg3, android.app.PendingIntent arg4, android.app.PendingIntent arg5) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_110, _this, arg0, arg1, arg2, arg3, arg4, arg5);
	}

	// Landroid/telephony/SmsManager;->sendMultipartTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V
	public static void m_111(android.telephony.SmsManager _this, String arg0, String arg1, java.util.ArrayList arg2, java.util.ArrayList arg3, java.util.ArrayList arg4) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_111, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/telephony/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V
	public static void m_112(android.telephony.SmsManager _this, String arg0, String arg1, String arg2, android.app.PendingIntent arg3, android.app.PendingIntent arg4) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_112, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/telephony/TelephonyManager;->getCellLocation()Landroid/telephony/CellLocation;
	public static android.telephony.CellLocation m_113(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			LocationPolicy.instance.android_telephony_TelephonyManager__getCellLocation();
		} catch (MonitorException e) {
			return (android.telephony.CellLocation) e.getValue();
		}
		return (android.telephony.CellLocation) INSTRUMENTATION.callReplacedObjectMethod(f_113, _this);
	}

	// Landroid/telephony/TelephonyManager;->getDeviceId()Ljava/lang/String;
	public static String m_114(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getDeviceId();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_114, _this);
	}

	// Landroid/telephony/TelephonyManager;->getDeviceSoftwareVersion()Ljava/lang/String;
	public static String m_115(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getDeviceSoftwareVersion();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_115, _this);
	}

	// Landroid/telephony/TelephonyManager;->getLine1Number()Ljava/lang/String;
	public static String m_116(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getLine1Number();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_116, _this);
	}

	// Landroid/telephony/TelephonyManager;->getNeighboringCellInfo()Ljava/util/List;
	public static java.util.List m_117(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			LocationPolicy.instance.android_telephony_TelephonyManager__getNeighboringCellInfo();
		} catch (MonitorException e) {
			return (java.util.List) e.getValue();
		}
		return (java.util.List) INSTRUMENTATION.callReplacedObjectMethod(f_117, _this);
	}

	// Landroid/telephony/TelephonyManager;->getSimSerialNumber()Ljava/lang/String;
	public static String m_118(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getSimSerialNumber();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_118, _this);
	}

	// Landroid/telephony/TelephonyManager;->getSubscriberId()Ljava/lang/String;
	public static String m_119(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getSubscriberId();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_119, _this);
	}

	// Landroid/telephony/TelephonyManager;->getVoiceMailAlphaTag()Ljava/lang/String;
	public static String m_120(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getVoiceMailAlphaTag();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_120, _this);
	}

	// Landroid/telephony/TelephonyManager;->getVoiceMailNumber()Ljava/lang/String;
	public static String m_121(android.telephony.TelephonyManager _this) throws Throwable {
		try {
			PhoneStatePolicy.instance.android_telephony_TelephonyManager__getVoiceMailNumber();
		} catch (MonitorException e) {
			return (String) e.getValue();
		}
		return (String) INSTRUMENTATION.callReplacedObjectMethod(f_121, _this);
	}

	// Landroid/telephony/TelephonyManager;->listen(Landroid/telephony/PhoneStateListener;I)V
	public static void m_122(android.telephony.TelephonyManager _this, android.telephony.PhoneStateListener arg0, int arg1) throws Throwable {
		try {
			LocationPolicy.instance.android_telephony_TelephonyManager__listen(_this, arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_122, _this, arg0, arg1);
	}

	// Landroid/telephony/gsm/SmsManager;->sendDataMessage(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)V
	public static void m_123(android.telephony.gsm.SmsManager _this, String arg0, String arg1, short arg2, byte[] arg3, android.app.PendingIntent arg4, android.app.PendingIntent arg5) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_123, _this, arg0, arg1, arg2, arg3, arg4, arg5);
	}

	// Landroid/telephony/gsm/SmsManager;->sendMultipartTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)V
	public static void m_124(android.telephony.gsm.SmsManager _this, String arg0, String arg1, java.util.ArrayList arg2, java.util.ArrayList arg3, java.util.ArrayList arg4) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_124, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/telephony/gsm/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V
	public static void m_125(android.telephony.gsm.SmsManager _this, String arg0, String arg1, String arg2, android.app.PendingIntent arg3, android.app.PendingIntent arg4) throws Throwable {
		try {
			SendSmsPolicy.instance.sendSms(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_125, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/webkit/WebView;->loadDataWithBaseURL(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
	public static void m_126(android.webkit.WebView _this, String arg0, String arg1, String arg2, String arg3, String arg4) throws Throwable {
		try {
			InternetPolicy.instance.android_webkit_WebView__loadUrl(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_126, _this, arg0, arg1, arg2, arg3, arg4);
	}

	// Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;)V
	public static void m_127(android.webkit.WebView _this, String arg0) throws Throwable {
		try {
			InternetPolicy.instance.android_webkit_WebView__loadUrl(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_127, _this, arg0);
	}

	// Landroid/webkit/WebView;->loadUrl(Ljava/lang/String;Ljava/util/Map;)V
	public static void m_128(android.webkit.WebView _this, String arg0, java.util.Map arg1) throws Throwable {
		try {
			InternetPolicy.instance.android_webkit_WebView__loadUrl(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_128, _this, arg0, arg1);
	}

	// Ljava/net/DatagramSocket;->connect(Ljava/net/InetAddress;I)V
	public static void m_129(java.net.DatagramSocket _this, java.net.InetAddress arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.java_net_DatagramSocket__connect(_this, arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_129, _this, arg0, arg1);
	}

	// Ljava/net/DatagramSocket;->connect(Ljava/net/SocketAddress;)V
	public static void m_130(java.net.DatagramSocket _this, java.net.SocketAddress arg0) throws Throwable {
		try {
			InternetPolicy.instance.java_net_DatagramSocket__connect(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_130, _this, arg0);
	}

	// Ljava/net/HttpURLConnection;->connect()V
	public static void m_131(java.net.HttpURLConnection _this) throws Throwable {
		try {
			InternetPolicy.instance.java_net_URLConnection__connect(_this);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_131, _this);
	}

	// Ljava/net/MulticastSocket;->joinGroup(Ljava/net/InetAddress;)V
	public static void m_132(java.net.MulticastSocket _this, java.net.InetAddress arg0) throws Throwable {
		try {
			InternetPolicy.instance.java_net_MulticastSocket__joinGroup(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_132, _this, arg0);
	}

	// Ljava/net/MulticastSocket;->joinGroup(Ljava/net/SocketAddress;Ljava/net/NetworkInterface;)V
	public static void m_133(java.net.MulticastSocket _this, java.net.SocketAddress arg0, java.net.NetworkInterface arg1) throws Throwable {
		try {
			InternetPolicy.instance.java_net_MulticastSocket__joinGroup(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_133, _this, arg0, arg1);
	}

	// Ljava/net/Socket;-><init>(Ljava/lang/String;I)V
	public static void m_134(java.net.Socket _this, String arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_134, _this, arg0, arg1);
	}

	// Ljava/net/Socket;-><init>(Ljava/lang/String;ILjava/net/InetAddress;I)V
	public static void m_135(java.net.Socket _this, String arg0, int arg1, java.net.InetAddress arg2, int arg3) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_135, _this, arg0, arg1, arg2, arg3);
	}

	// Ljava/net/Socket;-><init>(Ljava/lang/String;IZ)V
	public static void m_136(java.net.Socket _this, String arg0, int arg1, boolean arg2) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_136, _this, arg0, arg1, arg2);
	}

	// Ljava/net/Socket;-><init>(Ljava/net/InetAddress;I)V
	public static void m_137(java.net.Socket _this, java.net.InetAddress arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_137, _this, arg0, arg1);
	}

	// Ljava/net/Socket;-><init>(Ljava/net/InetAddress;ILjava/net/InetAddress;I)V
	public static void m_138(java.net.Socket _this, java.net.InetAddress arg0, int arg1, java.net.InetAddress arg2, int arg3) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_138, _this, arg0, arg1, arg2, arg3);
	}

	// Ljava/net/Socket;-><init>(Ljava/net/InetAddress;IZ)V
	public static void m_139(java.net.Socket _this, java.net.InetAddress arg0, int arg1, boolean arg2) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket_$init(arg0, arg1);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_139, _this, arg0, arg1, arg2);
	}

	// Ljava/net/Socket;->connect(Ljava/net/SocketAddress;)V
	public static void m_140(java.net.Socket _this, java.net.SocketAddress arg0) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket__connect(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_140, _this, arg0);
	}

	// Ljava/net/Socket;->connect(Ljava/net/SocketAddress;I)V
	public static void m_141(java.net.Socket _this, java.net.SocketAddress arg0, int arg1) throws Throwable {
		try {
			InternetPolicy.instance.java_net_Socket__connect(_this, arg0);
		} catch (MonitorException e) {
			return;
		}
		INSTRUMENTATION.callReplacedVoidMethod(f_141, _this, arg0, arg1);
	}

	// Ljava/net/URL;->openConnection()Ljava/net/URLConnection;
	public static java.net.URLConnection m_142(java.net.URL _this) throws Throwable {
		try {
			InternetPolicy.instance.java_net_URL__openConnection(_this);
		} catch (MonitorException e) {
			return (java.net.URLConnection) e.getValue();
		}
		return (java.net.URLConnection) INSTRUMENTATION.callReplacedObjectMethod(f_142, _this);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;)Lorg/apache/http/HttpResponse;
	public static org.apache.http.HttpResponse m_143(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.HttpHost arg0, org.apache.http.HttpRequest arg1) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (org.apache.http.HttpResponse) e.getValue();
		}
		return (org.apache.http.HttpResponse) INSTRUMENTATION.callReplacedObjectMethod(f_143, _this, arg0, arg1);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;
	public static Object m_144(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.HttpHost arg0, org.apache.http.HttpRequest arg1, org.apache.http.client.ResponseHandler arg2) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (Object) e.getValue();
		}
		return (Object) INSTRUMENTATION.callReplacedObjectMethod(f_144, _this, arg0, arg1, arg2);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;
	public static Object m_145(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.HttpHost arg0, org.apache.http.HttpRequest arg1, org.apache.http.client.ResponseHandler arg2, org.apache.http.protocol.HttpContext arg3) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (Object) e.getValue();
		}
		return (Object) INSTRUMENTATION.callReplacedObjectMethod(f_145, _this, arg0, arg1, arg2, arg3);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/HttpHost;Lorg/apache/http/HttpRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
	public static org.apache.http.HttpResponse m_146(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.HttpHost arg0, org.apache.http.HttpRequest arg1, org.apache.http.protocol.HttpContext arg2) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (org.apache.http.HttpResponse) e.getValue();
		}
		return (org.apache.http.HttpResponse) INSTRUMENTATION.callReplacedObjectMethod(f_146, _this, arg0, arg1, arg2);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;)Lorg/apache/http/HttpResponse;
	public static org.apache.http.HttpResponse m_147(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.client.methods.HttpUriRequest arg0) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (org.apache.http.HttpResponse) e.getValue();
		}
		return (org.apache.http.HttpResponse) INSTRUMENTATION.callReplacedObjectMethod(f_147, _this, arg0);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;)Ljava/lang/Object;
	public static Object m_148(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.client.methods.HttpUriRequest arg0, org.apache.http.client.ResponseHandler arg1) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (Object) e.getValue();
		}
		return (Object) INSTRUMENTATION.callReplacedObjectMethod(f_148, _this, arg0, arg1);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/client/ResponseHandler;Lorg/apache/http/protocol/HttpContext;)Ljava/lang/Object;
	public static Object m_149(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.client.methods.HttpUriRequest arg0, org.apache.http.client.ResponseHandler arg1, org.apache.http.protocol.HttpContext arg2) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (Object) e.getValue();
		}
		return (Object) INSTRUMENTATION.callReplacedObjectMethod(f_149, _this, arg0, arg1, arg2);
	}

	// Lorg/apache/http/impl/client/DefaultHttpClient;->execute(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
	public static org.apache.http.HttpResponse m_150(org.apache.http.impl.client.DefaultHttpClient _this, org.apache.http.client.methods.HttpUriRequest arg0, org.apache.http.protocol.HttpContext arg1) throws Throwable {
		try {
			InternetPolicy.instance.org_apache_http_impl_client_HttpClient__execute(_this, arg0);
		} catch (MonitorException e) {
			return (org.apache.http.HttpResponse) e.getValue();
		}
		return (org.apache.http.HttpResponse) INSTRUMENTATION.callReplacedObjectMethod(f_150, _this, arg0, arg1);
	}

}
