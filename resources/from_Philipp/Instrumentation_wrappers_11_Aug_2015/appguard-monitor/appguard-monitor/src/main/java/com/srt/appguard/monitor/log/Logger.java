package com.srt.appguard.monitor.log;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.srt.appguard.mobile.service.IMonitorService;
import com.srt.appguard.monitor.log.Message.Type;
import com.srt.appguard.monitor.policy.Policy;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

	private static final int LOGGER_SLEEP = 10 * 1000;
	private static final int LOGGER_WAKEUP = 60 * 1000;
	private static final int BULK_LOG_LIMIT = 100;

	private final static ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<Message>();
	
	private static Context context;
	private static ServiceConnection conn;
	private static IMonitorService service;
	private static Dispatcher dispatcher = null;
	
	private static class Dispatcher extends Thread {
		@Override
		public void run() {
			// dispatch messages as long as the service is alive
			while (service != null) {
				try {
					// Check if there are messages to send, otherwise wait here.
					synchronized (messages) {
						while (messages.isEmpty()) {
							messages.wait(LOGGER_WAKEUP);
						}
					}
					
					// Before we start, we wait shortly to collect messages
					Thread.sleep(LOGGER_SLEEP);

					// Re-check service because we slept
					if (service == null) {
						break;
					}

					// Get all log entries and remove duplicates
					TreeSet<Message> store = new TreeSet<Message>();
					Message message;
					do {
						message = messages.poll();
						if (message != null) {
							store.add(message);
						}
					} while (message != null);

					// Send them to AppGuard
					while (store.size() > 0) {
						int size = Math.min(store.size(), BULK_LOG_LIMIT);
						Message[] messages = new Message[size];
						for (int i = 0; i < messages.length; i++) {
							messages[i] = store.pollFirst();
						}
						service.bulkLog(messages);
					}
				} catch (InterruptedException e) {
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void create(final Context ctx, final String appguardPackageName, final String serviceClass) {
		if (context == null || service == null) {
			final Context applicationContext = ctx.getApplicationContext();
			if(applicationContext != null) {
				context = applicationContext;
			} else {
				context = ctx;
			}

			conn = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName name, IBinder binder) {
					service = IMonitorService.Stub.asInterface(binder);

					// start the dispatcher
					if(dispatcher == null || !dispatcher.isAlive()) {
						dispatcher = new Dispatcher();
						dispatcher.start();
					}
				}
				
				@Override
				public void onServiceDisconnected(ComponentName name) {
					service = null;
				}
			};

			final Intent intent = new Intent();
			intent.setClassName(appguardPackageName, serviceClass);
			context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
		}
	}

	public static void info(String msg) {
		addMessage(new Message(Type.INFO, msg));
	}

	public static void warn(String msg) {
		addMessage(new Message(Type.WARNING, msg));
	}

	public static void error(String msg) {
		addMessage(new Message(Type.ERROR, msg));
	}

	public static void debug(String msg) {
		addMessage(new Message(Type.DEBUG, msg));
	}

	public static void allow(String permission, Meta... metas) {
		addMessage(new Message(Type.ALLOW, permission, metas));
	}

	public static void deny(String permission, Meta... metas) {
		addMessage(new Message(Type.DENY, permission, metas));
	}

	private static void addMessage(Message message) {
		messages.add(message);
		synchronized (messages) {
			messages.notifyAll();
		}
	}

	public static String getTag(Class<?> clazz) {
		final String tag = clazz.getSimpleName();
		if (Policy.class.isAssignableFrom(clazz) && !Policy.class.equals(clazz)) {
			// cut off "Policy"
			return tag.substring(0, tag.length() - 6);
		}
		
		return tag;
	}
}
