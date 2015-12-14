package com.srt.appguard.monitor.policy.impl.phone;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.log.Meta;
import com.srt.appguard.monitor.log.Meta.MetaType;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class SendSmsPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static SendSmsPolicy instance;
	static {
		instance = new SendSmsPolicy();
	}

	private SendSmsPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.SEND_SMS;
	}

	@MapSignatures({
		"Landroid/telephony/SmsManager;->sendDataMessage(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)",
		"Landroid/telephony/SmsManager;->sendMultipartTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)",
		"Landroid/telephony/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)",

		"Landroid/telephony/gsm/SmsManager;->sendDataMessage(Ljava/lang/String;Ljava/lang/String;S[BLandroid/app/PendingIntent;Landroid/app/PendingIntent;)",
		"Landroid/telephony/gsm/SmsManager;->sendMultipartTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;)",
		"Landroid/telephony/gsm/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)"
	})
	public void sendSms(Object _this, String destAddress) throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.SEND_SMS, new Meta(MetaType.PHONE_NUMBER, destAddress));
		} else {
			Logger.deny(android.Manifest.permission.SEND_SMS, new Meta(MetaType.PHONE_NUMBER, destAddress));
			throw new MonitorException();
		}
	}
}
