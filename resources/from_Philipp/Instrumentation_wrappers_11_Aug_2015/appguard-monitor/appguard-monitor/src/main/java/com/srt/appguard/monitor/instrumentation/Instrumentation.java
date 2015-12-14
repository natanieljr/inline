package com.srt.appguard.monitor.instrumentation;

import android.util.Log;

import com.srt.appguard.monitor.Monitor;
import com.srt.appguard.monitor.log.Logger;

import de.larma.arthook.Unsafe;

public abstract class Instrumentation<T> {

	private static final String TAG = Logger.getTag(Monitor.class) + "::Instrumentation";

	static {
		try {
			System.loadLibrary("appguard-monitor");
		} catch (UnsatisfiedLinkError e) {
			Log.w(TAG, "Error while loading AppGuard library", e);
		}
	}

	// The ClassLoader used to lookup the guard methods
	public static ClassLoader[] classLoaders = new ClassLoader[]{
			Instrumentation.class.getClassLoader()
	};

	public static long getAddress(Object obj) {
		return Unsafe.getObjectAddress(obj);
	}

	/**
	 * Hock a virtual method (<code>target</code>) and replace it with another method (<code>hook</code>).
	 *
	 * @param targetClassName  The name of the class (e.g. <code>android/location/LocationManager</code>).
	 * @param targetMethodName The name of the method to hock (e.g. <code>getLastKnownLocation</code>).
	 * @param targetPrototype  The signature of the method (e.g. <code>(Ljava/lang/String;)Landroid/location/Location;</code>).
	 * @param hookClassName    The name of the class (e.g. <code>my/package/MyClass</code>).
	 * @param hookMethodName   The name of the method to hock (e.g. <code>getLastKnownLocation</code>).
	 * @param hookPrototype    The signature of the method (e.g. <code>(Ljava/lang/String;)Landroid/location/Location;</code>).
	 * @return A methodId object that can be used to call the original (<code>target</code>) method.
	 */
	public abstract T hookMethod(String targetClassName, String targetMethodName, String targetPrototype, String hookClassName, String hookMethodName, String hookPrototype);

	/**
	 * Hock a static method (<code>target</code>) and replace it with another method (<code>hook</code>).
	 *
	 * @param targetClassName  The name of the class (e.g. <code>android/location/LocationManager</code>).
	 * @param targetMethodName The name of the method to hock (e.g. <code>getLastKnownLocation</code>).
	 * @param targetPrototype  The signature of the method (e.g. <code>(Ljava/lang/String;)Landroid/location/Location;</code>).
	 * @param hookClassName    The name of the class (e.g. <code>my/package/MyClass</code>).
	 * @param hookMethodName   The name of the method to hock (e.g. <code>getLastKnownLocation</code>).
	 * @param hookPrototype    The signature of the method (e.g. <code>(Ljava/lang/String;)Landroid/location/Location;</code>).
	 * @return A methodId object that can be used to call the original (<code>target</code>) method.
	 */
	public abstract T hookStaticMethod(String targetClassName, String targetMethodName, String targetPrototype, String hookClassName, String hookMethodName, String hookPrototype);

	/**
	 * Call the original virtual method (returning void).
	 *
	 * @param methodId The methodId object returned by {@link #hookMethod}.
	 * @param _this    The this argument of the virtual method call.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract void callReplacedVoidMethod(T methodId, Object _this, Object... args) throws Throwable;

	/**
	 * Call the original virtual method (returning int).
	 *
	 * @param methodId The methodId object returned by {@link #hookMethod}.
	 * @param _this    The this argument of the virtual method call.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract int callReplacedIntMethod(T methodId, Object _this, Object... args) throws Throwable;

	/**
	 * Call the original virtual method (returning boolean).
	 *
	 * @param methodId The methodId object returned by {@link #hookMethod}.
	 * @param _this    The this argument of the virtual method call.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract boolean callReplacedBooleanMethod(T methodId, Object _this, Object... args) throws Throwable;

	/**
	 * Call the original virtual method (returning Object).
	 *
	 * @param methodId The methodId object returned by {@link #hookMethod}.
	 * @param _this    The this argument of the virtual method call.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract Object callReplacedObjectMethod(T methodId, Object _this, Object... args) throws Throwable;

	/**
	 * Call the original static method (returning void).
	 *
	 * @param methodId The methodId object returned by {@link #hookStaticMethod}.
	 * @param _clazz   The class the method is declared in.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract void callReplacedStaticVoidMethod(T methodId, Class<?> _clazz, Object... args) throws Throwable;

	/**
	 * Call the original static method (returning Object).
	 *
	 * @param methodId The methodId object returned by {@link #hookStaticMethod}.
	 * @param _clazz   The class the method is declared in.
	 * @param args     All other arguments of the method call.
	 * @throws Throwable The exception thrown by the original method.
	 */
	public abstract Object callReplacedStaticObjectMethod(T methodId, Class<?> _clazz, Object... args) throws Throwable;

	public abstract int getNumGuards();
}
