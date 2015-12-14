package com.srt.appguard.util;

import java.util.HashMap;
import java.util.Map;

import org.jf.dexlib.TypeIdItem;

public class TypeUtils {

	protected static class PrimitiveType {
		public Class<?> objectType;
		public String descriptor;
		
		public PrimitiveType(final Class<?> objectType, final String descriptor) {
			this.objectType = objectType;
			this.descriptor = descriptor;
		}
	}

	protected static final Map<Class<?>, PrimitiveType> primitives = new HashMap<Class<?>, PrimitiveType>();
	static {
		primitives.put(void.class,		new PrimitiveType(null, "V"));
		primitives.put(byte.class,		new PrimitiveType(Byte.class, "B"));
		primitives.put(long.class,		new PrimitiveType(Long.class, "J"));
		primitives.put(short.class,		new PrimitiveType(Short.class, "S"));
		primitives.put(float.class,		new PrimitiveType(Float.class, "F"));
		primitives.put(double.class,	new PrimitiveType(Double.class, "D"));
		primitives.put(int.class,		new PrimitiveType(Integer.class, "I"));
		primitives.put(boolean.class,	new PrimitiveType(Boolean.class, "Z"));
		primitives.put(char.class,		new PrimitiveType(Character.class, "C"));
	}

	public static String getTypeDescriptor(final Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return primitives.get(clazz).descriptor;
		}

		final String descriptor = clazz.getName().replace('.', '/'); 
		if (clazz.isArray()) {
			return descriptor;
		}

		return "L" + descriptor + ";";
	}

	public static Class<?> getClass(final String typeDescriptor) {
		try {
			if (isPrimitive(typeDescriptor)) {
				// Class.forName("I") throws ClassNotFound, Class.forName("[I") doesn't
				// thus, we look up the array type and return the component class
				// TODO clean up
				return Class.forName("[" + typeDescriptor).getComponentType();
			}
			
			return Class.forName(getClassName(typeDescriptor));
		} catch (final ClassNotFoundException e) {
			return null;
		}
	}

	public static Class<?> getClass(final TypeIdItem type) {
		return getClass(type.getTypeDescriptor());
	}

	protected static String getClassName(final String typeDescriptor) {
		if (isPrimitive(typeDescriptor)) {
			return typeDescriptor;
		}
		
		final String name = typeDescriptor.replace('/', '.');
		if (isArray(typeDescriptor)) {
			return name;
		}

		return name.substring(1, name.length() - 1);
	}

	protected static String getClassName(final TypeIdItem type) {
		return getClassName(type.getTypeDescriptor());
	}

	public static Class<?> getObjectType(final Class<?> clazz) {
		if (!clazz.isPrimitive()) {
			return clazz;
		}
		return primitives.get(clazz).objectType;
	}

	public static Class<?> getObjectType(final TypeIdItem type) {
		return getObjectType(getClass(type.getTypeDescriptor()));
	}

	public static boolean isPrimitive(final String typeDescriptor) {
		// TODO clean up
		return  "V".equals(typeDescriptor) ||
				"B".equals(typeDescriptor) ||
				"J".equals(typeDescriptor) ||
				"S".equals(typeDescriptor) ||
				"F".equals(typeDescriptor) ||
				"D".equals(typeDescriptor) ||
				"I".equals(typeDescriptor) ||
				"Z".equals(typeDescriptor) ||
				"C".equals(typeDescriptor);
	}

	public static boolean isPrimitive(final TypeIdItem type) {
		return isPrimitive(type.getTypeDescriptor());
	}

	public static boolean isArray(final String typeDescriptor) {
		// TODO clean up
		return typeDescriptor.charAt(0) == '[';
	}

	public static boolean isArray(final TypeIdItem type) {
		return isArray(type.getTypeDescriptor());
	}

	public static boolean isWide(final Class<?> clazz) {
		return long.class.equals(clazz) || double.class.equals(clazz);
	}

	public static boolean isWide(final TypeIdItem type) {
		//final String descriptor = type.getTypeDescriptor();
		//return "J".equals(descriptor) || "D".equals(descriptor);
		return type.getRegisterCount() == 2;
	}
	
	public static boolean isVoid(final TypeIdItem type) {
		return "V".equals(type.getTypeDescriptor());
	}
}
