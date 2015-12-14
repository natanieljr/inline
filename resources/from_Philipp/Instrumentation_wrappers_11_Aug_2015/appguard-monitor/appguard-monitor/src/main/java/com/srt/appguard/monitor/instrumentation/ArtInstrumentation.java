package com.srt.appguard.monitor.instrumentation;

import android.util.Log;

import com.srt.appguard.monitor.Monitor;
import com.srt.appguard.monitor.log.Logger;
import com.srt.appguard.monitor.util.ReflectionHelper;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.larma.arthook.ArtHook;
import de.larma.arthook.ArtMethod;
import de.larma.arthook.DebugHelper;

public class ArtInstrumentation extends Instrumentation<ArtInstrumentation.AbstractHandle> {

	private static final String TAG = Logger.getTag(Monitor.class) + "::ArtInstrumentation";

	private int numGuards;

	public ArtInstrumentation() {
		DebugHelper.DEBUG = false;
	}

	@Override
	public AbstractHandle hookMethod(String targetClassName, String targetMethodName, String targetPrototype, String hookClassName, String hookMethodName, String hookPrototype) {
		final AccessibleObject targetMethod = ReflectionHelper.findAbstractMethod(targetClassName, targetMethodName, targetPrototype, classLoaders);
		if (targetMethod == null) {
			Log.w(TAG, "Unable to resolve method: " + targetClassName + "->" + targetMethodName + targetPrototype);
			return null;
		}

		final Method hookMethod = ReflectionHelper.findMethod(hookClassName, hookMethodName, hookPrototype, classLoaders);
		if (hookMethod == null) {
			Log.w(TAG, "Unable to resolve method: " + hookClassName + "->" + hookMethodName + hookPrototype);
			return null;
		}

		// Hook method
		final ArtMethod originalArtMethod = hook(targetMethod, hookMethod);

		// Get handle to call original method
		final AbstractHandle original = getAbstractHandle(targetMethod, originalArtMethod);

		numGuards++;

		return original;
	}

	private ArtMethod getArtMethod(AccessibleObject targetMethod) {
		if (targetMethod instanceof Method) {
			return ArtMethod.of((Method) targetMethod);
		} else {
			return ArtMethod.of((Constructor<?>) targetMethod);
		}
	}

	private ArtMethod hook(AccessibleObject targetMethod, Method hookMethod) {
		if (targetMethod instanceof Method) {
			return ArtHook.hook((Method) targetMethod, hookMethod);
		} else {
			return ArtHook.hook((Constructor<?>) targetMethod, hookMethod);
		}
	}

	private AbstractHandle getAbstractHandle(AccessibleObject targetMethod, ArtMethod originalArtMethod) {
		if (targetMethod instanceof Method) {
			final Method m = originalArtMethod.newMethod();
			return new MethodHandle(m);
		} else {
			final Constructor<?> c = originalArtMethod.newConstructor();
			return new ConstructorHandle(c);
		}
	}

	@Override
	public AbstractHandle hookStaticMethod(String targetClassName, String targetMethodName, String targetPrototype, String hookClassName, String hookMethodName, String hookPrototype) {
		return hookMethod(targetClassName, targetMethodName, targetPrototype, hookClassName, hookMethodName, hookPrototype);
	}

	@Override
	public void callReplacedVoidMethod(AbstractHandle method, Object _this, Object... args) throws Throwable {
		try {
			method.invoke(_this, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public int callReplacedIntMethod(AbstractHandle method, Object _this, Object... args) throws Throwable {
		try {
			return (int) method.invoke(_this, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public boolean callReplacedBooleanMethod(AbstractHandle method, Object _this, Object... args) throws Throwable {
		try {
			return (boolean) method.invoke(_this, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public Object callReplacedObjectMethod(AbstractHandle method, Object _this, Object... args) throws Throwable {
		try {
			return method.invoke(_this, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public void callReplacedStaticVoidMethod(AbstractHandle method, Class<?> _clazz, Object... args) throws Throwable {
		try {
			method.invoke(null, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public Object callReplacedStaticObjectMethod(AbstractHandle method, Class<?> _clazz, Object... args) throws Throwable {
		try {
			return method.invoke(null, args);
		} catch (InvocationTargetException e) {
			// Unpack target exception and throw it
			throw e.getTargetException();
		} catch (IllegalAccessException e) {
			throw new LibArtException("Error while invoking original method.", e);
		} catch (InstantiationException e) {
			throw new LibArtException("Error while invoking original method.", e);
		}
	}

	@Override
	public int getNumGuards() {
		return numGuards;
	}

	public interface AbstractHandle {
		public abstract Object invoke(Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException;

		public abstract String getName();
	}

	public static class MethodHandle implements AbstractHandle {

		private final Method m;

		public MethodHandle(Method m) {
			this.m = m;
		}

		@Override
		public Object invoke(Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
			return m.invoke(receiver, args);
		}

		@Override
		public String getName() {
			return m.getName();
		}
	}

	public static class ConstructorHandle implements AbstractHandle {

		private final Constructor<?> c;

		public ConstructorHandle(Constructor<?> c) {
			this.c = c;
		}

		@Override
		public Object invoke(Object _this, Object[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
			return c.newInstance(args);
		}

		@Override
		public String getName() {
			return c.getName();
		}
	}
}
