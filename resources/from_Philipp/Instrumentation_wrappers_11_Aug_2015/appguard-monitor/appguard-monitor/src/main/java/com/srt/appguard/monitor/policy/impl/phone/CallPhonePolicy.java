package com.srt.appguard.monitor.policy.impl.phone;

import android.content.Intent;

import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.impl.IntentPolicy;

public class CallPhonePolicy extends IntentPolicy {

	/* All Policies are required to be singletons. */
	public static CallPhonePolicy instance;
	static {
		instance = new CallPhonePolicy();
	}

	private CallPhonePolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.CALL_PHONE;
	}

	@Override
	protected boolean isIntentAllowed(Intent intent) {
		if (intent == null) {
			return true;
		}
		
		final String action = intent.getAction();
		final boolean matches = Intent.ACTION_CALL.equals(action);
		
		if (matches) {
			if (allowed) {
				Logger.allow(android.Manifest.permission.CALL_PHONE, new Meta(MetaType.PHONE_NUMBER, intent.getDataString()));
			} else {
				Logger.deny(android.Manifest.permission.CALL_PHONE, new Meta(MetaType.PHONE_NUMBER, intent.getDataString()));
			}
			return allowed;
		}

		return true;
	}
}
