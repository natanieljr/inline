package com.srt.appguard.monitor.policy.impl;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.policy.Policy;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;

import android.app.Activity;
import android.content.Intent;

public abstract class IntentPolicy extends SinglePermissionPolicy {

	protected abstract boolean isIntentAllowed(Intent intent);

	@MapSignatures({
		//"Landroid/app/Activity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/Activity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/Activity;->startActivityIfNeeded(Landroid/content/Intent;I)"
		/*
		"Landroid/accounts/AccountAuthenticatorActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/accounts/AccountAuthenticatorActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/accounts/AccountAuthenticatorActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",
		
		"Landroid/app/ActivityGroup;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/ActivityGroup;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/ActivityGroup;->startActivityIfNeeded(Landroid/content/Intent;I)",
		
		"Landroid/app/AliasActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/AliasActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/AliasActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",
		
		"Landroid/app/ExpandableListActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/ExpandableListActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/ExpandableListActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",

		"Landroid/app/ListActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/ListActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/ListActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",

		"Landroid/app/NativeActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/NativeActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/NativeActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",

		"Landroid/app/TabActivity;->startActivity(Landroid/content/Intent;)",
		"Landroid/app/TabActivity;->startActivityForResult(Landroid/content/Intent;I)",
		"Landroid/app/TabActivity;->startActivityIfNeeded(Landroid/content/Intent;I)",
		*/

		//"Landroid/app/Service;->startActivity(Landroid/content/Intent;)",
		//"Landroid/app/IntentService;->startActivity(Landroid/content/Intent;)",
		//"Landroid/accessibilityservice/AccessibilityService;->startActivity(Landroid/content/Intent;)",
		//"Landroid/inputmethodservice/AbstractInputMethodService;->startActivity(Landroid/content/Intent;)",
		//"Landroid/inputmethodservice/InputMethodService;->startActivity(Landroid/content/Intent;)",
		//"Landroid/service/wallpaper/WallpaperService;->startActivity(Landroid/content/Intent;)",
		//"Landroid/speech/RecognitionService;->startActivity(Landroid/content/Intent;)",

		//"Landroid/content/Context;->startActivity(Landroid/content/Intent;)"
		//"Landroid/content/ContextWrapper;->startActivity(Landroid/content/Intent;)"
	})
	public void startActivity(Object _this, Intent intent) throws Exception {
		if (!isIntentAllowed(intent)) {
			throw new MonitorException();
		}
	}
	
	@MapSignatures({
		"Landroid/app/Activity;->startActivityFromChild(Landroid/app/Activity;Landroid/content/Intent;I)"
	})
	public void startActivity(Activity _this, Activity activity, Intent intent) throws Exception {
		if (!isIntentAllowed(intent)) {
			throw new MonitorException();
		}
	}
}
