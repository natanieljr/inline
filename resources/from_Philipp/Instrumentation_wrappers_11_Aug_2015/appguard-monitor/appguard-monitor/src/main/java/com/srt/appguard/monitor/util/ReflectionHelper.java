package com.srt.appguard.monitor.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class ReflectionHelper {

	public static final String CONSTRUCTOR_NAME = "<init>";

	/**
	 * Find a method/constructor by its name.
	 *
	 * @param className  Name of the class (e.g. "<code>android/app/Activity</code>").
	 * @param methodName Name of the method (e.g. "<code>onResume</code>" or "<init>" for constructors).
	 * @param prototype  Arguments of the method (e.g. "<code>(Landroid/content/Intent;I)Z</code>").
	 * @param loaders    A list of classLoaders used for class lookup.
	 * @return The resolved method object or <code>null</code>.
	 */
	public static AccessibleObject findAbstractMethod(String className, String methodName, String prototype, ClassLoader[] loaders) {
		if (CONSTRUCTOR_NAME.equals(methodName)) {
			return findConstructor(className, prototype, loaders);
		} else {
			return findMethod(className, methodName, prototype, loaders);
		}
	}

	/**
	 * Find a constructor by its name.
	 *
	 * @param className Name of the class (e.g. "<code>android/app/Activity</code>").
	 * @param prototype Arguments of the method (e.g. "<code>(Landroid/content/Intent;I)Z</code>").
	 * @param loaders   A list of classLoaders used for class lookup.
	 * @return The resolved method object or <code>null</code>.
	 */
	public static Constructor<?> findConstructor(String className, String prototype, ClassLoader[] loaders) {
		final Class<?> clazz = getClass("L" + className + ";", loaders);
		if (clazz != null) {
			final int bracketOpenIdx = prototype.indexOf('(');
			final int bracketCloseIdx = prototype.indexOf(')');
			final String parameters = prototype.substring(bracketOpenIdx + 1, bracketCloseIdx);

			final Class<?>[] types = getParameterTypes(parameters, loaders);
			if (types != null) {
				return getConstructor(clazz, types);
			}
		}
		return null;
	}

	/**
	 * Find a method by its name.
	 *
	 * @param className  Name of the class (e.g. "<code>android/app/Activity</code>").
	 * @param methodName Name of the method (e.g. "<code>onResume</code>").
	 * @param prototype  Arguments of the method (e.g. "<code>(Landroid/content/Intent;I)Z</code>").
	 * @param loaders    A list of classLoaders used for class lookup.
	 * @return The resolved method object or <code>null</code>.
	 */
	public static Method findMethod(String className, String methodName, String prototype, ClassLoader[] loaders) {
		final Class<?> clazz = getClass("L" + className + ";", loaders);
		if (clazz != null) {
			final int bracketOpenIdx = prototype.indexOf('(');
			final int bracketCloseIdx = prototype.indexOf(')');
			final String parameters = prototype.substring(bracketOpenIdx + 1, bracketCloseIdx);

			final Class<?>[] types = getParameterTypes(parameters, loaders);
			if (types != null) {
				return getMethod(clazz, methodName, types);
			}
		}
		return null;
	}

	private static Class<?> getClass(String classDescriptor, ClassLoader[] loaders) {
		for (ClassLoader loader : loaders) {
			Class<?> clazz = getClass(classDescriptor, loader);
			if (clazz != null) {
				return clazz;
			}
		}
		return null;
	}

	public static Class<?> getClass(final String classDescriptor, ClassLoader loader) {
		try {
			if (TypeUtils.isPrimitive(classDescriptor)) {
				final Class<?> arrayClass = Class.forName("[" + TypeUtils.getClassName(classDescriptor), true, loader);
				return arrayClass.getComponentType();
			} else {
				return Class.forName(TypeUtils.getClassName(classDescriptor), true, loader);
			}
		} catch (Throwable e) {
			// Silence is king!
		}
		return null;
	}

	private static Method getMethod(Class<?> clazz, String methodName, Class<?>[] types) {
		try {
			return clazz.getMethod(methodName, types);
		} catch (Throwable e) {
			// Silence is king!
		}
		try {
			return clazz.getDeclaredMethod(methodName, types);
		} catch (Throwable e) {
			// Silence is king!
		}
		return null;
	}

	private static Constructor<?> getConstructor(Class<?> clazz, Class<?>[] types) {
		if (types != null) {
			try {
				return clazz.getConstructor(types);
			} catch (NoSuchMethodException e) {
				// Silence is king!
			}
			try {
				return clazz.getDeclaredConstructor(types);
			} catch (NoSuchMethodException e) {
				// Silence is king!
			}
		}
		return null;
	}

	private static Class<?>[] getParameterTypes(final String parameterTypes, ClassLoader[] loaders) {
		final List<Class<?>> types = new LinkedList<>();
		for (final String type : parseParameterDescriptors(parameterTypes)) {
			final Class<?> clazz = getClass(type, loaders);
			if (clazz == null) {
				return null;
			}
			types.add(clazz);
		}

		return types.toArray(new Class<?>[types.size()]);
	}

	private static String[] parseParameterDescriptors(final String parameterTypes) {
		final List<String> types = new LinkedList<>();
		final int strLength = parameterTypes.length();

		int offset = 0;
		String arrayPrefix = "";
		while (offset < strLength) {
			String type = String.valueOf(parameterTypes.charAt(offset));
			if (TypeUtils.isArray(type)) {
				arrayPrefix += "[";
				++offset;
			} else {
				if (!TypeUtils.isPrimitive(type)) {
					type = parameterTypes.substring(offset, parameterTypes.indexOf(';', offset) + 1);
				}

				types.add(arrayPrefix + type);
				offset += type.length();
				arrayPrefix = "";
			}
		}

		return types.toArray(new String[types.size()]);
	}
}
