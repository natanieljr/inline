package com.srt.appguard.monitor.policy.impl.hardware;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class RecordAudioPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final RecordAudioPolicy instance;
	static {
		instance = new RecordAudioPolicy();
	}

	private RecordAudioPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.RECORD_AUDIO;
	}

	@MapSignatures({
		"Landroid/media/AudioRecord;-><init>(IIIII)"
	})
	public void android_media_AudioRecord_$init() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.RECORD_AUDIO);
		} else {
			Logger.deny(android.Manifest.permission.RECORD_AUDIO);
			throw new IllegalArgumentException();
		}
	}
	
	@MapSignatures({
		"Landroid/media/MediaRecorder;->setAudioSource(I)"
	})
	public void android_media_MediaRecorder__setAudioSource() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.RECORD_AUDIO);
		} else {
			Logger.deny(android.Manifest.permission.RECORD_AUDIO);
			throw new MonitorException();
		}
	}

	@MapSignatures({
		"Landroid/speech/SpeechRecognizer;->cancel()",
		"Landroid/speech/SpeechRecognizer;->startListening(Landroid/content/Intent;)",
		"Landroid/speech/SpeechRecognizer;->stopListening()",
		"Landroid/speech/SpeechRecognizer;->handleCancelMessage()",
		"Landroid/speech/SpeechRecognizer;->handleStartListening(Landroid/content/Intent;)",
		"Landroid/speech/SpeechRecognizer;->handleStopMessage()"
	})
	public void android_speech_SpeechRecognizer_$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.RECORD_AUDIO);
		} else {
			Logger.deny(android.Manifest.permission.RECORD_AUDIO);
			throw new MonitorException();
		}
	}
}
