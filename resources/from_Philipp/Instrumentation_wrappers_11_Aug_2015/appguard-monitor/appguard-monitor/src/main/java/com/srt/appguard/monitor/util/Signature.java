package com.srt.appguard.monitor.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Signature implements Comparable<Signature> {

	public static final String CONSTRUCTOR_NAME = "<init>";

	// HACK
	// TODO cleanup
	private static final Set<String> unresolvableTypes;

	static {
		unresolvableTypes = new HashSet<String>();

		unresolvableTypes.add("Landroid/os/ICancellationSignal;");
		unresolvableTypes.add("Landroid/content/ContentProviderNative;");
	}

	private final String classDescriptor;
	private final String methodName;
	private final String[] parameterDescriptors;
	private final String returnDescriptor;
	private final boolean isStatic;
	private final boolean isConstructor;

	public Signature(String classDescriptor, String methodName, String[] parameterDescriptors, String returnDescriptor, boolean isStatic,
					 boolean isConstructor) {
		this.classDescriptor = classDescriptor;
		this.methodName = methodName;
		this.parameterDescriptors = parameterDescriptors;
		this.returnDescriptor = returnDescriptor;
		this.isStatic = isStatic;
		this.isConstructor = isConstructor;
	}

	public static Signature fromMethod(final Method method, final Class<?> clazz) {
		Class<?>[] parameterClasses = method.getParameterTypes();
		String[] parameterTypes = getParameterDescriptors(parameterClasses);
		String classDescriptor = TypeUtils.getTypeDescriptor(clazz);
		String returnDescriptor = TypeUtils.getTypeDescriptor(method.getReturnType());
		return new Signature(classDescriptor, method.getName(), parameterTypes, returnDescriptor, Modifier.isStatic(method.getModifiers()), false);
	}

	public static Signature fromConstructor(final Constructor<?> constructor, final Class<?> clazz) {
		Class<?>[] parameterClasses = constructor.getParameterTypes();
		String[] parameterTypes = getParameterDescriptors(parameterClasses);
		String classDescriptor = TypeUtils.getTypeDescriptor(clazz);
		return new Signature(classDescriptor, CONSTRUCTOR_NAME, parameterTypes, "V", false, true);
	}

	public static Signature fromIdentifier(final String identifier, ClassLoader[] loaders) {
		final int dashIdx = identifier.indexOf('-');
		return fromName(identifier.substring(0, dashIdx), identifier.substring(dashIdx + 2), loaders);
	}

	public static Signature fromName(final String classDescriptor, final String methodPrototype, ClassLoader[] loaders) {
		final int bracketOpenIdx = methodPrototype.indexOf('(');
		final int bracketCloseIdx = methodPrototype.indexOf(')');

		final String methodName = methodPrototype.substring(0, bracketOpenIdx);
		final String parameters = methodPrototype.substring(bracketOpenIdx + 1, bracketCloseIdx);

		Class<?> clazz = getClass(classDescriptor, loaders);
		if (CONSTRUCTOR_NAME.equals(methodName)) {
			Constructor<?> constructor = getConstructor(clazz, parameters, loaders);
			if (constructor != null) {
				return fromConstructor(constructor, clazz);
			}
		} else {
			Method method = getMethod(clazz, methodName, parameters, loaders);
			if (method != null) {
				return fromMethod(method, clazz);
			}
		}

		// Unable to use reflection, so do it manually
		if (CONSTRUCTOR_NAME.equals(methodName)) {
			String[] parameterDescriptors = parseParameterDescriptors(parameters);
			return new Signature(classDescriptor, methodName, parameterDescriptors, "V", false, true);
		} else {
			int indexOf = methodPrototype.indexOf(';', bracketCloseIdx);
			if (indexOf < 0 || methodPrototype.length() <= indexOf + 1) {
				return null;
			}

			String[] parameterDescriptors = parseParameterDescriptors(parameters);
			String isStaticStr = methodPrototype.substring(bracketCloseIdx + 1, indexOf);
			String returnType = methodPrototype.substring(indexOf + 1);
			boolean isStatic = isStaticStr.equals("static");
			return new Signature(classDescriptor, methodName, parameterDescriptors, returnType, isStatic, false);
		}
	}

	private static Class<?> getClass(String classDescriptor, ClassLoader[] loaders) {
		Class<?> clazz = null;
		for (ClassLoader loader : loaders) {
			clazz = getClass(classDescriptor, loader);
			if (clazz != null) {
				break;
			}
		}
		return clazz;
	}

	public static Class<?> getClass(final String classDescriptor, ClassLoader loader) {
		Class<?> clazz = null;
		try {
			if (TypeUtils.isPrimitive(classDescriptor)) {
				Class<?> arrayClass = Class.forName("[" + TypeUtils.getClassName(classDescriptor), true, loader);
				clazz = arrayClass.getComponentType();
			} else {
				clazz = Class.forName(TypeUtils.getClassName(classDescriptor), true, loader);
			}
		} catch (Throwable e) {
		}
		return clazz;
	}

	private static Method getMethod(Class<?> clazz, String methodName, String parameters, ClassLoader[] loaders) {
		Class<?>[] types = getParameterTypes(parameters, loaders);
		if (types != null) {
			try {
				Method method = clazz.getMethod(methodName, types);
				return method;
			} catch (Throwable e) {
			}
			try {
				Method method = clazz.getDeclaredMethod(methodName, types);
				return method;
			} catch (Throwable e) {
			}
		}
		return null;
	}

	private static Constructor<?> getConstructor(Class<?> clazz, String parameters, ClassLoader[] loaders) {
		Class<?>[] types = getParameterTypes(parameters, loaders);
		if (types != null) {
			try {
				Constructor<?> constructor = clazz.getConstructor(types);
				return constructor;
			} catch (NoSuchMethodException e) {
			}
			try {
				Constructor<?> constructor = clazz.getDeclaredConstructor(types);
				return constructor;
			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}

	private static Class<?>[] getParameterTypes(final String parameterTypes, ClassLoader[] loaders) {
		final List<Class<?>> types = new LinkedList<Class<?>>();
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
		final List<String> types = new LinkedList<String>();
		final int strlen = parameterTypes.length();

		int offset = 0;
		String arrayPrefix = "";
		while (offset < strlen) {
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

	private static String[] getParameterDescriptors(Class<?>[] classes) {
		String[] descriptors = new String[classes.length];
		for (int i = 0; i < descriptors.length; i++) {
			descriptors[i] = TypeUtils.getTypeDescriptor(classes[i]);
		}
		return descriptors;
	}

	public String getFullClassName() {
		return TypeUtils.getJavaType(classDescriptor);
	}

	public String getSimpleClassName() {
		String className = getFullClassName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	public String getJniClassName() {
		return TypeUtils.getJniType(classDescriptor);
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodSignature() {
		return "(" + getParameterDescriptors() + ")" + returnDescriptor;
	}

	/**
	 * This one may include the _this argument of virtual methods.
	 *
	 * @return
	 */
	public String getNativeMethodSignature() {
		String thisType = isStatic ? "" : getResolvableTypeDescriptor(classDescriptor);
		return "(" + thisType + getResolvableParameterDescriptors() + ")" + returnDescriptor;
	}

	/**
	 * Get the Java signature of a method.
	 * <p/>
	 * Example: "<code>java.lang.String _this, int arg0, java.lang.Object arg1</code>"
	 *
	 * @return The signature string.
	 */
	public String getJavaMethodSignature() {
		StringBuilder builder = new StringBuilder();

		// Add this for non-statics
		if (!isStatic) {
			String thisType = getResolvableType(classDescriptor);
			builder.append(thisType + " _this");
			if (parameterDescriptors.length > 0) {
				builder.append(", ");
			}
		}

		// Arguments
		for (int i = 0; i < parameterDescriptors.length; i++) {
			String type = getResolvableType(parameterDescriptors[i]);
			builder.append(type + " arg" + i);
			if (i < parameterDescriptors.length - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	private String getResolvableType(String typeDescriptor) {
		if (!isResolvableType(typeDescriptor)) {
			return "java.lang.Object";
		}

		return TypeUtils.getJavaType(typeDescriptor);
	}

	private String getResolvableTypeDescriptor(String typeDescriptor) {
		if (!isResolvableType(typeDescriptor)) {
			return "Ljava/lang/Object;";
		}

		return typeDescriptor;
	}

	public int getParameterCount() {
		return parameterDescriptors.length;
	}

	public String getReturnDescriptor() {
		return returnDescriptor;
	}

	public String getReturnType() {
		return getResolvableType(returnDescriptor);
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public Method getResolvableMethod(ClassLoader[] loaders) {
		Class<?> clazz = getClass(classDescriptor, loaders);
		Method method = getMethod(clazz, methodName, getParameterDescriptors(), loaders);
		if (method != null) {
			return method;
		}

		return null;
	}

	private String getParameterDescriptors() {
		StringBuilder builder = new StringBuilder();
		for (String parameterDescriptor : parameterDescriptors) {
			builder.append(parameterDescriptor);
		}
		return builder.toString();
	}

	private String getResolvableParameterDescriptors() {
		StringBuilder builder = new StringBuilder();
		for (String parameterDescriptor : parameterDescriptors) {
			builder.append(getResolvableTypeDescriptor(parameterDescriptor));
		}

		return builder.toString();
	}

	private boolean isResolvableType(String typeDescriptor) {
		// TODO cleanup
		if (unresolvableTypes.contains(typeDescriptor)) {
			return false;
		}

		Class<?> clazz = TypeUtils.getClass(typeDescriptor);
		return clazz != null && Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * Generate the name of the receiver method.
	 *
	 * @return A string consisting of the class name and method name.
	 */
	public String getIdentifier() {
		return classDescriptor + "->" + methodName + getMethodSignature();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Signature)) {
			return false;
		}
		return getIdentifier().equals(((Signature) o).getIdentifier());
	}

	@Override
	public int hashCode() {
		return getIdentifier().hashCode();
	}

	@Override
	public String toString() {
		return getIdentifier();
	}

	@Override
	public int compareTo(Signature another) {
		return getIdentifier().compareTo(another.getIdentifier());
	}
}