package de.uds.infsec.instrumentation.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link Signature} object identifies a Java method
 */
public class Signature implements Comparable<Signature> {

	public static final String CONSTRUCTOR_NAME = "<init>";

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

	// Notation: com.foo.bar.Test.method
	public static Signature fromDescriptor(final String descriptor) {
		final List<Signature> signatures = findSignatures(descriptor);
		if (signatures.size() < 1) {
			return null;
		} else if (signatures.size() > 1) {
			// TODO match arguments
			throw new IllegalArgumentException("Method descriptor '" + descriptor + "' is ambiguous");
		}
		return signatures.get(0);
	}

	// Notation: com.foo.bar.Test->method
	public static Signature[] matchSignatures(final String fromDescriptor, final String toDescriptor) {
		final List<Signature> fromSignatures = findSignatures(fromDescriptor);
		final List<Signature> toSignatures = findSignatures(toDescriptor);

		return matchSignatures(fromSignatures, toSignatures);
	}

	// Notation: com.foo.bar.Test->method
	public static Signature[] matchSignatures(final String fromDescriptor, final Signature to) {
		final List<Signature> fromSignatures = findSignatures(fromDescriptor);
		final List<Signature> toSignatures = new LinkedList<Signature>();
		toSignatures.add(to);

		return matchSignatures(fromSignatures, toSignatures);
	}

	private static Signature[] matchSignatures(final List<Signature> fromSignatures, final List<Signature> toSignatures) {
		// TODO handle multimatches
		// TODO complexity
		for (final Signature to : toSignatures) {
			for (final Signature from : fromSignatures) {
				if (from.isCompatibleTarget(to)) {
					final Signature[] result = new Signature[2];
					result[0] = from;
					result[1] = to;
					return result;
				}
			}
		}
		return null;
	}

	private boolean isCompatibleTarget(Signature to) {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final Class<?>[] fromTypes = getParameterTypes(this.parameterDescriptors, loader);
		final Class<?>[] toTypes;
		if (this.isStatic) {
			toTypes = getParameterTypes(to.parameterDescriptors, loader);
		} else {
			final String[] params = Arrays.copyOfRange(to.parameterDescriptors, 1, to.parameterDescriptors.length);
			toTypes = getParameterTypes(params, loader);
		}

		if (fromTypes.length != toTypes.length) {
			return false;
		}

		// compare the parameter types
		for (int i = 0; i < fromTypes.length; i++) {
			Class<?> fromType = fromTypes[i];
			Class<?> toType = toTypes[i];

			// always match if the source type is an object and the target is an object-type
			// to account for unresolvable types
			if (Object.class.equals(fromType) && !toType.isPrimitive()) {
				continue;
			}

			// match if the target type is assignable from the source type
			if (toType.isAssignableFrom(fromType)) {
				continue;
			}

			// types didn't match -> incompatible
			return false;
		}

		// all types matched -> compatible
		return true;
	}

	public static List<Signature> findSignatures(final String descriptor) {
		// parse class and method name
		// TODO cleanup
		int signatureIdx = descriptor.indexOf('(');
		if (signatureIdx < 0) {
			signatureIdx = descriptor.length();
		}

		int methodIdx = descriptor.lastIndexOf('-', signatureIdx);
		if (methodIdx < 0) {
			throw new IllegalArgumentException("Method descriptor '" + descriptor + "' does not include a method name.");
		}

		String className = descriptor.substring(0, methodIdx);
		String methodName = descriptor.substring(methodIdx + 2, signatureIdx);

		try {
			final List<Signature> signatures = new LinkedList<Signature>();
			final Class<?> clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
			for (final Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(methodName)) {
					signatures.add(Signature.fromMethod(method, clazz));
				}
			}

			return signatures;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}

		return new LinkedList<Signature>();
	}

	public static Signature fromName(final String classDescriptor, final String methodPrototype, ClassLoader[] loaders) {
		final int bracketOpenIdx = methodPrototype.indexOf('(');
		final int bracketCloseIdx = methodPrototype.indexOf(')');

		final String methodName = methodPrototype.substring(0, bracketOpenIdx);
		final String parameters = methodPrototype.substring(bracketOpenIdx + 1, bracketCloseIdx);

		if(CONSTRUCTOR_NAME.equals(methodName)) {
			for (ClassLoader loader : loaders) {
				Class<?> clazz = getClass(classDescriptor, loader);
				Constructor<?> constructor = getConstructor(clazz, parameters, loader);
				if(constructor != null) {
					return fromConstructor(constructor, clazz);
				}
			}
		} else {
			for (ClassLoader loader : loaders) {
				Class<?> clazz = getClass(classDescriptor, loader);
				Method method = getMethod(clazz, methodName, parameters, loader);
				if(method != null) {
					return fromMethod(method, clazz);
				}
			}
		}

		// Unable to use reflection, so do it manually
		if(CONSTRUCTOR_NAME.equals(methodName)) {
			String[] parameterDescriptors = parseParameterDescriptors(parameters);
			return new Signature(classDescriptor, methodName, parameterDescriptors, "V", false, true);
		} else {
			int indexOf = methodPrototype.indexOf(';', bracketCloseIdx);
			if(indexOf < 0 || methodPrototype.length() <= indexOf + 1) {
				return null;
			}

			String[] parameterDescriptors = parseParameterDescriptors(parameters);
			String isStaticStr = methodPrototype.substring(bracketCloseIdx + 1, indexOf);
			String returnType = methodPrototype.substring(indexOf + 1);
			boolean isStatic = isStaticStr.equals("static");
			return new Signature(classDescriptor, methodName, parameterDescriptors, returnType, isStatic, false);
		}
	}

	public static Class<?> getClass(final String classDescriptor, ClassLoader loader) {
		Class<?> clazz = null;
		try {
			if(TypeUtils.isPrimitive(classDescriptor)) {
				Class<?> arrayClass = Class.forName("[" + TypeUtils.getClassName(classDescriptor), true, loader);
				clazz = arrayClass.getComponentType();
			} else {
				clazz = Class.forName(TypeUtils.getClassName(classDescriptor), true, loader);
			}
		} catch (Throwable e) {
		}
		return clazz;
	}

	private static Method getMethod(Class<?> clazz, String methodName, String parameters, ClassLoader loader) {
		try {
			Class<?>[] types = getParameterTypes(parseParameterDescriptors(parameters), loader);
			if(types != null) {
				Method method = clazz.getMethod(methodName, types);
				return method;
			}
		} catch (Throwable e) {
		}
		return null;
	}

	private static Constructor<?> getConstructor(Class<?> clazz, String parameters, ClassLoader loader) {
		try {
			Class<?>[] types = getParameterTypes(parseParameterDescriptors(parameters), loader);
			if(types != null) {
				Constructor<?> constructor = clazz.getConstructor(types);
				return constructor;
			}
		} catch (NoSuchMethodException e) {
		}
		return null;
	}

	private static Class<?>[] getParameterTypes(final String[] parameterTypes, ClassLoader loader) {
		final List<Class<?>> types = new LinkedList<Class<?>>();
		for (final String type : parameterTypes) {
			final Class<?> clazz = getClass(type, loader);
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
	 */
	public String getNativeMethodSignature() {
		String thisType = isStatic ? "" : getResolvableTypeDescriptor(classDescriptor);
		return "(" + thisType + getResolvableParameterDescriptors() + ")" + returnDescriptor;
	}

	/**
	 * Get the Java signature of a method.
	 *
	 * Example: "<code>java.lang.String _this, int arg0, java.lang.Object arg1</code>"
	 *
	 * @return The signature string.
	 */
	public String getJavaMethodSignature() {
		StringBuilder builder = new StringBuilder();

		// Add this for non-statics
		if(!isStatic) {
			String thisType = getResolvableType(classDescriptor);
			builder.append(thisType + " _this");
			if(parameterDescriptors.length > 0) {
				builder.append(", ");
			}
		}

		// Arguments
		for (int i = 0; i < parameterDescriptors.length; i++) {
			String type = getResolvableType(parameterDescriptors[i]);
			builder.append(type + " arg" + i);
			if(i < parameterDescriptors.length - 1) {
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

	public Class<?> getResolvableClass(ClassLoader[] loaders) {
		for (ClassLoader loader : loaders) {
			Class<?> clazz = getClass(classDescriptor, loader);
			if (clazz != null) {
				return clazz;
			}
		}
		return null;
	}

	public Method getResolvableMethod(ClassLoader[] loaders) {
		for (ClassLoader loader : loaders) {
			Class<?> clazz = getClass(classDescriptor, loader);
			if (clazz == null) {
				continue;
			}

			Method method = getMethod(clazz, methodName, getParameterDescriptors(), loader);
			if (method != null) {
				return method;
			}
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