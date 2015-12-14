package com.srt.appguard.monitor.policy.impl.system;

import com.srt.appguard.monitor.exception.MonitorException;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.policy.annotation.MapSignatures;
import com.srt.appguard.monitor.policy.impl.SinglePermissionPolicy;

public class WakeLockPolicy extends SinglePermissionPolicy {

	/* All Policies are required to be singletons. */
	public static final WakeLockPolicy instance;

	static {
		instance = new WakeLockPolicy();
	}

	private WakeLockPolicy() {
	}

	@Override
	protected String getPermissionName() {
		return android.Manifest.permission.WAKE_LOCK;
	}

	@MapSignatures({
			"Landroid/bluetooth/HeadsetBase;->acquireWakeLock();V", // returns V
			//"Landroid/bluetooth/HeadsetBase;->finalize()", // returns V
			"Landroid/bluetooth/HeadsetBase;->releaseWakeLock();V", // returns V
			//"Landroid/media/AsyncPlayer;->acquireWakeLock()", // returns V
			//"Landroid/media/AsyncPlayer;->play(Landroid/content/Context;Landroid/net/Uri;ZI)", // returns V
			//"Landroid/media/AsyncPlayer;->releaseWakeLock()", // returns V
			//"Landroid/media/AsyncPlayer;->stop()", // returns V
			//"Landroid/media/MediaPlayer;->start()", // returns V
			//"Landroid/media/MediaPlayer;->stop()", // returns V
			//"Landroid/net/wifi/IWifiManager$Stub$Proxy;->releaseWifiLock(Landroid/os/IBinder;)", // returns Z
			"Landroid/net/wifi/WifiManager$WifiLock;->acquire()", // returns V
			//"Landroid/net/wifi/WifiManager$WifiLock;->finalize()", // returns V
			"Landroid/net/wifi/WifiManager$WifiLock;->release()", // returns V
			//"Landroid/os/IPowerManager$Stub$Proxy;->releaseWakeLock(Landroid/os/IBinder;I)", // returns V
			"Landroid/os/PowerManager$WakeLock;->acquire()", // returns V
			"Landroid/os/PowerManager$WakeLock;->acquire(J)", // returns V
			"Landroid/os/PowerManager$WakeLock;->release()", // returns V
			"Landroid/os/PowerManager$WakeLock;->release(I)", // returns V

			// Added with AppGuard 2.2.7
			"Landroid/net/sip/SipAudioCall;->startAudio()", //returns V
			"Landroid/media/MediaPlayer;->setWakeMode(Landroid/content/Context;I)" // returns V
	})
	public void wakeLock_$catchAll() throws Exception {
		if (allowed) {
			Logger.allow(android.Manifest.permission.WAKE_LOCK);
		} else {
			Logger.deny(android.Manifest.permission.WAKE_LOCK);
			// return false for IWifiManager$Stub$Proxy;->releaseWifiLock()
			throw new MonitorException(false);
		}
	}
}
