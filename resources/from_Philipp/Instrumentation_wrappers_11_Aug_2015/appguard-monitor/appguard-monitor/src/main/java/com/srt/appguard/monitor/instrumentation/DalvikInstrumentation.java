package com.srt.appguard.monitor.instrumentation;

import android.util.Log;

import com.srt.appguard.monitor.Monitor;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.util.Signature;

import java.lang.reflect.Method;

public class DalvikInstrumentation extends Instrumentation<Integer> {

	private static final String TAG = Logger.getTag(Monitor.class) + "::DalvikInstrumentation";

	static {
		init();
	}

	// We keep an array of the guard method objects. This is used 
	// by the trampoline methods to lookup their corresponding guard 
	// method. It also prevents the method objects from being GC'ed.
	private static Method[] guards;
	private static long guardsPtr;
	private static int guardsIdx;

	private static void init() {
		// TODO resize the array as needed or initialize it differently
		guards = new Method[256];

		// Get the address of the guard array by looking at its hashcode.
		// This circumvents the problem of indirect references being passed to jni
		// functions since ICS.
		guardsPtr = getAddress(guards);

		//Log.d(TAG, "Instrumentation initialized (GuardsArray at " + Integer.toHexString(guardsPtr) + ")");
	}

	private static native int replaceMethodNative(String className, String methodName, String prototype, long guardsPtr, int guardsIdx);

	private static native int replaceStaticMethodNative(String className, String methodName, String prototype, long guardsPtr, int guardsIdx);

	// Debug helpers
	public static native byte[] readMemory(int address, int length);

	public static native void printRegisterMap(String className, String methodName, String signature, boolean isStatic);

	public static native int getInstructionPtr(String className, String methodName, String signature, boolean isStatic);

	public static native int getInstructionSize(String className, String methodName, String signature, boolean isStatic);

	public static void dumpMem(int address, int length) {
		dumpMem(address, length, false);
	}

	public static void dumpMem(int address, int length, boolean reverse) {
		int columns = 6;

		int addr = reverse ? address - length : address;
		byte[] content = readMemory(addr, length);

		StringBuilder sbHex = new StringBuilder();
		sbHex.append(String.format("%08x: ", addr));

		StringBuilder sbAscii = new StringBuilder();
		sbAscii.append("| ");

		int i = 0;
		for (byte value : content) {
			if (value >= 32 && value <= 127) {
				sbAscii.append((char) value);
			} else {
				sbAscii.append(".");
			}

			sbHex.append(String.format("%02x", value));

			i++;

			// add space after every 4th byte
			if (i % 4 == 0) {
				sbHex.append(" ");
			}

			// new line after #columns
			if (i % (columns * 4) == 0 || i == length) {
				Log.d(TAG, sbHex.toString() + sbAscii.toString());

				sbHex = new StringBuilder();
				sbHex.append(String.format("%08x: ", (addr + i)));

				sbAscii = new StringBuilder();
				sbAscii.append("| ");
			}
		}
	}

	@Override
	public Integer hookMethod(
			String dstClassName, String dstMethodName, String dstPrototype,
			String srcClassName, String srcMethodName, String srcPrototype) {

		//Log.d(TAG, "Replacing method " + dstClassName + "->" + dstMethodName + dstPrototype);

		// parse the guard method signature
		// TODO cleanup
		final String srcDescriptor = "L" + srcClassName + ";";
		final Signature srcSignature = Signature.fromName(srcDescriptor, srcMethodName + srcPrototype, classLoaders);
		if (srcSignature == null) {
			Log.w(TAG, "Failed to create signature: " + srcClassName + "->" + srcMethodName + srcPrototype);
			return 0;
		}

		// lookup the guard method object
		final Method guardMethod = srcSignature.getResolvableMethod(classLoaders);
		if (guardMethod == null) {
			Log.w(TAG, "Failed to lookup guard method: " + srcClassName + "->" + srcMethodName + srcPrototype);
			return 0;
		}

		// replace the method
		int orgMethod = replaceMethodNative(
				dstClassName, dstMethodName, dstPrototype,
				guardsPtr, guardsIdx);

		if (orgMethod == 0) {
			// something went wrong with the instrumentation of this method
			Log.w(TAG, "Instrumentation failed: " + dstClassName + "->" + dstMethodName + dstPrototype);
		} else {
			// add the guard to guards array
			guards[guardsIdx++] = guardMethod;
		}

		/* show the new instructions
		int insns = getInstructionPtr(dstClassName, dstMethodName, dstPrototype, false);
		int size = getInstructionSize(dstClassName, dstMethodName, dstPrototype, false);

		Log.d(TAG, "New instructions (size " + size + ") for " + dstClassName + "->" + dstMethodName + dstPrototype);
		dumpMem(insns, size * 2 + 20);
		*/

		return orgMethod;
	}

	@Override
	public Integer hookStaticMethod(
			String dstClassName, String dstMethodName, String dstPrototype,
			String srcClassName, String srcMethodName, String srcPrototype) {

		//Log.d(TAG, "Replacing method " + dstClassName + "->" + dstMethodName + dstPrototype);

		// parse the guard method signature
		// TODO cleanup
		final String srcDescriptor = "L" + srcClassName + ";";
		final Signature srcSignature = Signature.fromName(srcDescriptor, srcMethodName + srcPrototype, classLoaders);
		if (srcSignature == null) {
			Log.w(TAG, "Failed to create signature: " + srcClassName + "->" + srcMethodName + srcPrototype);
			return 0;
		}

		// lookup the guard method object
		final Method guardMethod = srcSignature.getResolvableMethod(classLoaders);
		if (guardMethod == null) {
			Log.w(TAG, "Failed to lookup guard method: " + srcClassName + "->" + srcMethodName + srcPrototype);
			return 0;
		}

		// replace the method
		int orgMethod = replaceStaticMethodNative(
				dstClassName, dstMethodName, dstPrototype,
				guardsPtr, guardsIdx);

		if (orgMethod == 0) {
			// something went wrong with the instrumentation of this method
			Log.w(TAG, "Instrumentation failed: " + dstClassName + "->" + dstMethodName + dstPrototype);
		} else {
			// add the guard to guards array
			guards[guardsIdx++] = guardMethod;
		}

		/* show the new instructions
		int insns = getInstructionPtr(dstClassName, dstMethodName, dstPrototype, true);
		int size = getInstructionSize(dstClassName, dstMethodName, dstPrototype, true);

		Log.d(TAG, "New instructions (size " + size + ") for " + dstClassName + "->" + dstMethodName + dstPrototype);
		dumpMem(insns, size * 2 + 20);
		*/

		return orgMethod;
	}

	@Override
	public void callReplacedVoidMethod(Integer methodId, Object _this, Object... args) {
		callReplacedVoidMethod((int) methodId, _this, args);
	}

	@Override
	public int callReplacedIntMethod(Integer methodId, Object _this, Object... args) {
		return callReplacedIntMethod((int) methodId, _this, args);
	}

	@Override
	public boolean callReplacedBooleanMethod(Integer methodId, Object _this, Object... args) {
		return callReplacedBooleanMethod((int) methodId, _this, args);
	}

	@Override
	public Object callReplacedObjectMethod(Integer methodId, Object _this, Object... args) {
		return callReplacedObjectMethod((int) methodId, _this, args);
	}

	@Override
	public void callReplacedStaticVoidMethod(Integer methodId, Class<?> _clazz, Object... args) {
		callReplacedStaticVoidMethod((int) methodId, _clazz, args);
	}

	@Override
	public Object callReplacedStaticObjectMethod(Integer methodId, Class<?> _clazz, Object... args) {
		return callReplacedStaticObjectMethod((int) methodId, _clazz, args);
	}

	public native void callReplacedVoidMethod(int methodId, Object _this, Object... args);

	public native int callReplacedIntMethod(int methodId, Object _this, Object... args);

	public native boolean callReplacedBooleanMethod(int methodId, Object _this, Object... args);

	public native Object callReplacedObjectMethod(int methodId, Object _this, Object... args);

	public native void callReplacedStaticVoidMethod(int methodId, Class<?> _clazz, Object... args);

	public native Object callReplacedStaticObjectMethod(int methodId, Class<?> _clazz, Object... args);

	public int getNumGuards() {
		return guardsIdx;
	}
}	
