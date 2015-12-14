package com.srt.appguard.monitor.policy;

import java.util.ArrayList;
import java.util.List;

import com.srt.appguard.monitor.policy.impl.RefreshConfigPolicy;
import com.srt.appguard.monitor.policy.impl.content.BrowserPolicy;
import com.srt.appguard.monitor.policy.impl.content.CalendarPolicy;
import com.srt.appguard.monitor.policy.impl.content.ContactsPolicy;
import com.srt.appguard.monitor.policy.impl.content.MediaStorePolicy;
import com.srt.appguard.monitor.policy.impl.content.SettingsPolicy;
import com.srt.appguard.monitor.policy.impl.content.SmsPolicy;
import com.srt.appguard.monitor.policy.impl.hardware.CameraPolicy;
import com.srt.appguard.monitor.policy.impl.hardware.ModifyAudioSettingsPolicy;
import com.srt.appguard.monitor.policy.impl.hardware.RecordAudioPolicy;
import com.srt.appguard.monitor.policy.impl.internet.InternetPolicy;
import com.srt.appguard.monitor.policy.impl.location.LocationPolicy;
import com.srt.appguard.monitor.policy.impl.location.MockLocationPolicy;
import com.srt.appguard.monitor.policy.impl.phone.CallPhonePolicy;
import com.srt.appguard.monitor.policy.impl.phone.PhoneStatePolicy;
import com.srt.appguard.monitor.policy.impl.phone.SendSmsPolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeNetworkStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeWifiMulticastStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.ChangeWifiStatePolicy;
import com.srt.appguard.monitor.policy.impl.system.GetTasksPolicy;
import com.srt.appguard.monitor.policy.impl.system.WakeLockPolicy;

public class PolicyFactory {

	private static List<Policy> policies = null;

	public static List<Policy> getPolicies() {
		if (policies == null) {
			policies = new ArrayList<Policy>();
			registerPolicies();
		}
		return policies;
	}

	private static void registerPolicies() {
		register(CameraPolicy.instance);
		register(InternetPolicy.instance);
		register(ContactsPolicy.instance);
		register(LocationPolicy.instance);
		register(CallPhonePolicy.instance);
		register(SendSmsPolicy.instance);
		register(PhoneStatePolicy.instance);

		register(RecordAudioPolicy.instance);
		register(ModifyAudioSettingsPolicy.instance);

		register(CalendarPolicy.instance);
		register(BrowserPolicy.instance);
		register(SettingsPolicy.instance);
		register(SmsPolicy.instance);

		register(MockLocationPolicy.instance);

		register(ChangeNetworkStatePolicy.instance);
		register(ChangeWifiMulticastStatePolicy.instance);
		register(ChangeWifiStatePolicy.instance);
		//register(ClearAppCachePolicy.instance);
		register(GetTasksPolicy.instance);
		register(WakeLockPolicy.instance);

		register(MediaStorePolicy.instance);

		register(RefreshConfigPolicy.instance);

		// DISABLED
		// register(HttpsRedirectPolicy.instance);
	}

	private static void register(Policy policy) {
		policies.add(policy);
	}
}
