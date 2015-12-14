package com.srt.appguard.monitor.util;

import java.util.HashMap;
import java.util.Map;

public class TypeUtils {

	protected static class PrimitiveType {
		public final Class<?> type;
		public final Class<?> objectType;
		public final String descriptor;

		public PrimitiveType(final Class<?> type, final Class<?> objectType, final String descriptor) {
			this.type = type;
			this.objectType = objectType;
			this.descriptor = descriptor;
		}
	}

	protected static final Map<Class<?>, PrimitiveType> primitives = new HashMap<Class<?>, PrimitiveType>();
	protected static final Map<String, PrimitiveType> primitivesByDescriptor = new HashMap<String, PrimitiveType>();

	static {
		PrimitiveType[] primitiveTypes = {
				new PrimitiveType(Void.TYPE, Void.class, "V"),
				new PrimitiveType(Byte.TYPE, Byte.class, "B"),
				new PrimitiveType(Long.TYPE, Long.class, "J"),
				new PrimitiveType(Short.TYPE, Short.class, "S"),
				new PrimitiveType(Float.TYPE, Float.class, "F"),
				new PrimitiveType(Double.TYPE, Double.class, "D"),
				new PrimitiveType(Integer.TYPE, Integer.class, "I"),
				new PrimitiveType(Boolean.TYPE, Boolean.class, "Z"),
				new PrimitiveType(Character.TYPE, Character.class, "C")
		};

		for (PrimitiveType primitiveType : primitiveTypes) {
			primitives.put(primitiveType.type, primitiveType);
			primitivesByDescriptor.put(primitiveType.descriptor, primitiveType);
		}
	}

	/**
	 * Returns the type descriptor for the given type.
	 * 
	 * For example <code>Ljava/lang/String;</code> for object types or
	 * <code>I</code> for primitive types.
	 * 
	 * @param clazz
	 *            The type as a {@link Class} object.
	 * @return The string representation of the type.
	 */
	public static String getTypeDescriptor(final Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return primitives.get(clazz).descriptor;
		}

		final String descriptor = clazz.getName().replace('.', '/'); //.replace('$', '/');
		if (clazz.isArray()) {
			return descriptor;
		}

		return "L" + descriptor + ";";
	}

	public static Class<?> getClass(final String typeDescriptor) {
		try {
			if (isPrimitive(typeDescriptor)) {
				return primitivesByDescriptor.get(typeDescriptor).type;
			}

			return Class.forName(getClassName(typeDescriptor));
		} catch (final ClassNotFoundException e) {
			return null;
		}
	}

	public static String getClassName(final String typeDescriptor) {
		if (isPrimitive(typeDescriptor)) {
			return typeDescriptor;
		}

		final String name = typeDescriptor.replace('/', '.');
		if (isArray(typeDescriptor)) {
			return name;
		}

		return name.substring(1, name.length() - 1);
	}

	public static boolean isPrimitive(final String typeDescriptor) {
		return primitivesByDescriptor.containsKey(typeDescriptor);
	}

	public static boolean isArray(final String typeDescriptor) {
		return typeDescriptor.charAt(0) == '[';
	}

	public static String getJavaType(String typeDescriptor) {
		String arraySuffix = "";
		while (isArray(typeDescriptor)) {
			arraySuffix += "[]";
			typeDescriptor = typeDescriptor.substring(1);
		}

		String type;
		if ("I".equals(typeDescriptor)) {
			type = "int";
		} else if ("J".equals(typeDescriptor)) {
			type = "long";
		} else if ("D".equals(typeDescriptor)) {
			type = "double";
		} else if ("F".equals(typeDescriptor)) {
			type = "float";
		} else if ("Z".equals(typeDescriptor)) {
			type = "boolean";
		} else if ("S".equals(typeDescriptor)) {
			type = "short";
		} else if ("C".equals(typeDescriptor)) {
			type = "char";
		} else if ("B".equals(typeDescriptor)) {
			type = "byte";
		} else if ("V".equals(typeDescriptor)) {
			type = "void";
		} else {
			type = typeDescriptor.replace('/', '.').replace('$', '.').substring(1, typeDescriptor.length() - 1);
		}
		return type + arraySuffix;
	}

	public static String getJniType(String typeDescriptor) {
		if (isPrimitive(typeDescriptor)) {
			return typeDescriptor;
		}
		return typeDescriptor.substring(1, typeDescriptor.length() - 1);
	}
}
