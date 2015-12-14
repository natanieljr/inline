package com.srt.appguard.monitor.policy.impl.hardware;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class ModifyAudioSettingsPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final ModifyAudioSettingsPolicy instance;

	static {
		instance = new ModifyAudioSettingsPolicy();
	}

	private ModifyAudioSettingsPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.MODIFY_AUDIO_SETTINGS;
	}

	@MapSignatures({
			"Landroid/media/AudioManager;->setBluetoothScoOn(Z)", // returns V
			"Landroid/media/AudioManager;->setMicrophoneMute(Z)", // returns V
			"Landroid/media/AudioManager;->setMode(I)", // returns V
			"Landroid/media/AudioManager;->setParameter(Ljava/lang/String;Ljava/lang/String;)", // returns V
			"Landroid/media/AudioManager;->setParameters(Ljava/lang/String;)", // returns V
			"Landroid/media/AudioManager;->setSpeakerphoneOn(Z)", // returns V
			"Landroid/media/AudioManager;->startBluetoothSco()", // returns V
			"Landroid/media/AudioManager;->stopBluetoothSco()",// returns V

			// Added with AppGuard 2.2.7
			"Landroid/net/sip/SipAudioCall;->void setSpeakerMode(Z);V"// returns V acc. to doc. modify_audio_settings perm. needed

			// TODO
			//"Landroid/media/AudioService;->setBluetoothScoOn(Z)", // returns V
			//"Landroid/media/AudioService;->setMode(ILandroid/os/IBinder;)", // returns V
			//"Landroid/media/AudioService;->setSpeakerphoneOn(Z)", // returns V
			//"Landroid/media/AudioService;->startBluetoothSco(Landroid/os/IBinder;)", // returns V
			//"Landroid/media/AudioService;->stopBluetoothSco(Landroid/os/IBinder;)" // returns V

			//"Landroid/media/IAudioService$Stub$Proxy;->setBluetoothScoOn(Z)", // returns V
			//"Landroid/media/IAudioService$Stub$Proxy;->setMode(ILandroid/os/IBinder;)", // returns V
			//"Landroid/media/IAudioService$Stub$Proxy;->setSpeakerphoneOn(Z)", // returns V
			//"Landroid/media/IAudioService$Stub$Proxy;->startBluetoothSco(Landroid/os/IBinder;)", // returns V
			//"Landroid/media/IAudioService$Stub$Proxy;->stopBluetoothSco(Landroid/os/IBinder;)" // returns V
	})
	public void modifyAudioSettings_$catchAll_V() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
		} else {
			Logger.deny(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
			throw new MonitorException();
		}
	}

	@MapSignatures({
			"Landroid/media/AudioManager;->isBluetoothA2dpOn()", // returns Z
			"Landroid/media/AudioManager;->isWiredHeadsetOn()", // returns Z
			"Landroid/server/BluetoothA2dpService;->resumeSink(Landroid/bluetooth/BluetoothDevice;);Z", // returns Z

			// Added with AppGuard 2.2.7
			"Landroid/bluetooth/BluetoothHeadset;->stopVoiceRecognition(Landroid/bluetooth/BluetoothDevice;)", // returns Z acc. to doc. bluetooth perm. needed
			"Landroid/bluetooth/BluetoothHeadset;->startVoiceRecognition(Landroid/bluetooth/BluetoothDevice;)" // returns Z

			//"Landroid/bluetooth/IBluetoothA2dp$Stub$Proxy;->resumeSink(Landroid/bluetooth/BluetoothDevice;)" // returns Z
	})
	public void modifyAudioSettings_$catchAll_Z() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
		} else {
			Logger.deny(android.Manifest.permission.MODIFY_AUDIO_SETTINGS);
			throw new MonitorException(false);
		}
	}
}
