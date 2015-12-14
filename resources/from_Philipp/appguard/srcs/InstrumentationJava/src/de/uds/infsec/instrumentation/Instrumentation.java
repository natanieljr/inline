package de.uds.infsec.instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import de.uds.infsec.instrumentation.annotation.Id;
import de.uds.infsec.instrumentation.annotation.Redirect;
import de.uds.infsec.instrumentation.util.Signature;

/**
 * Instrumentation API
 * <p>
 * This class allows you to instrument Java methods to redirect them to different implementations.
 * </p>
 *
 * <h4>Example Usage</h4>
 *
 * <pre>
 * package com.example;
 *
 * class Test {
 *
 * 	public Test() {
 * 		Test.foo(); // prints "foo() called"
 *
 * 		int orgMethod = Instrumentation.redirectMethod(
 * 			"com.example.Test->foo",
 * 			"com.example.Test->bar"
 *
 *		Test.foo(); // prints "bar() called"
 *
 *		Instrumentation.callStaticVoidMethod(
 *			orgMethod, Test.class); // prints "foo() called"
 *	}
 *
 *	public static void foo() {
 *		System.out.println("foo() called");
 *	}
 *
 *	public static void bar() {
 *		System.out.println("bar() called");
 *	}
 * }
 * </pre>
 *
 * In this example, the static function <code>foo()</code> is redirect to the function <code>bar()</code>. Three things are important
 * to note: First, function <code>foo()</code> and function <code>bar()</code> need to have identical (or compatible) signatures.
 * Second, when calling the original function, you have to use the appropriate <code>Instrumentation.call*Method()}
 * function, depending on the return type of the function hooked. Third, the redirection target must be a static function.
 * Let's have a look at another example to see how to redirect virtual functions.
 *
 * <pre>
 * package com.example;
 *
 * class Test {
 *
 * 	public Test() {
 * 		Test.foo(); // prints "foo() called"
 *
 * 		int orgMethod = Instrumentation.redirectMethod(
 * 			"com.example.Test->foo",
 * 			"com.example.Test->bar"
 *
 *		Test.foo(); // prints "bar() called"
 *
 *		Instrumentation.callVoidMethod(
 *			orgMethod, Test.class); // prints "foo() called"
 *	}
 *
 *	public void foo() {
 *		System.out.println("foo() called");
 *	}
 *
 *	public static void bar(Test _this) {
 *		System.out.println("bar() called");
 *	}
 * }
 * </pre>
 *
 * In this example, the function <code>foo()</code> is virtual, i.e. it is invoked on an object (<code>this</code>). The object is
 * implicitly passed to the function as the first argument. In order to redirect to the function <code>bar()</code>, the <code>bar()</code>
 * function needs to declare this object as its first argument. Finally, note that the call to <code>Instrumentation.call*Method()</code>
 * has changed.
 *
 * Alternatively, a Annotation-based API is supported.
 *
 * <pre>
 * class Monitor {
 *
 *  public Monitor {
 *  	Instrumentation.processClass(Monitor.class);
 *  }
 *
 * 	{@literal @}Redirect("com.example.Test->foo")
 * 	{@literal @}Id("&lt;unique_identifier&gt;")
 * 	public static void bar() {
 * 		System.out.println("bar() called");
 *
 * 		Instrumentation.callStaticVoidMethod("&lt;unique_identifier&gt;", Test.class);
 * 	}
 * }
 * </pre>
 *
 * <pre>
 * class Monitor {
 *
 *  public Monitor {
 *  	Instrumentation.processClass(Monitor.class);
 *  }
 *
 * 	{@literal @}Redirect("com.example.Test->foo")
 * 	public static void bar() {
 * 		System.out.println("bar() called");
 *
 * 		class $ {};
 * 		Instrumentation.callStaticVoidMethod($.class, Test.class);
 * 	}
 * }
 * </pre>
 *
 */
public final class Instrumentation {

	private static final String TAG = "Instrumentation";

	static {
		try {
      // KJA
      Log.w(TAG, "KJA DEBUG");
			NativeLibrary.load("instrumentation", Instrumentation.class.getClassLoader());
			init();
		} catch (UnsatisfiedLinkError e) {
			Log.w(TAG, "Error while loading Instrumentation library", e);
		}
	}

	// We keep an array of the guard method objects. This is used
	// by the trampoline methods to lookup their corresponding guard
	// method. It also prevents the method objects from being GC'ed.
	private static Method[] guards;
    private static int guardsPtr;
    private static int guardsIdx;

    // Map guard method IDs to their original methods.
    // We need to this to "magically" call the correct original method
    // when a call*Method() function is passed a caller reference.
    private static Map<String, Integer> originals;

    private static void init() {
    	// TODO array size
    	guards = new Method[1024];

    	// Get the real memory address of the guard array. This
    	// circumvents the problem of indirect references being
    	// passed to JNI functions since ICS.
    	guardsPtr = getAddress(guards);

    	originals = new HashMap<String, Integer>();
    }

    private static int getAddress(Object obj) {
        try {
        	final Class<?> unsafeClazz = Class.forName("sun.misc.Unsafe");
            final Field field = unsafeClazz.getDeclaredField("THE_ONE");
            field.setAccessible(true);
            final Object unsafe = field.get(null);

            final Object[] array = new Object[] {obj};
            final Method methArrayBaseOffset = unsafeClazz.getMethod("arrayBaseOffset", Class.class);
            final int baseOffset = (Integer) methArrayBaseOffset.invoke(unsafe, Object[].class);

        	final Method methGetInt = unsafeClazz.getMethod("getInt", Object.class, long.class);
        	return (Integer) methGetInt.invoke(unsafe, array, baseOffset);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to get address of guards array, falling back to hashCode()");

            return obj.hashCode();
        }
    }

    /**
     * Redirects all calls to the method specified by {@code fromDescriptor} to the method specified by {@code toDescriptor}.
     *
     * @param fromDescriptor A string specifying a Java method to be redirected, e.g. com.foobar.ClassA->method
     * @param toDescriptor A string specifying the Java method to redirect to, e.g. com.foobar.ClassB->method
     * @return A handle to the original implementation of the redirected Java method or 0 on failure
     **/
    public static int redirectMethod(String fromDescriptor, String toDescriptor) {
    	final Signature[] matches = Signature.matchSignatures(fromDescriptor, toDescriptor);
    	if (matches == null) {
    		Log.w(TAG, "Failed to redirect method " + fromDescriptor + " to " + toDescriptor);
    		return 0;
    	}

    	final Signature fromSignature = matches[0];
    	final Signature toSignature = matches[1];

    	return redirectMethod(fromSignature, toSignature);
    }

    /**
     * Redirects all calls to the method specified by {@code from} to the method specified by {@code to}.
     *
     * @param from A {@link Signature} object specifying a Java method to be redirected, e.g. com.foobar.ClassA->method
     * @param to A {@link Signature} object specifying the Java method to redirect to, e.g. com.foobar.ClassB->method
     * @return A handle to the original implementation of the redirected Java method or 0 on failure
     */
    public static int redirectMethod(Signature from, Signature to) {
		// lookup the guard method object
    	final ClassLoader[] loaders = new ClassLoader[] {
    			Thread.currentThread().getContextClassLoader(),
    			Instrumentation.class.getClassLoader()
    	};

		final Method guardMethod = to.getResolvableMethod(loaders);
		if (guardMethod == null) {
			Log.w(TAG, "Failed to lookup guard method: " + to);
			return 0;
		}

		// replace the method
		int orgMethod;
		if (from.isStatic()) {
			orgMethod = redirectStaticMethodNative(
					from.getResolvableClass(loaders), from.getMethodName(), from.getMethodSignature(),
					guardsPtr, guardsIdx);
		} else {
			orgMethod = redirectMethodNative(
					from.getResolvableClass(loaders), from.getMethodName(), from.getMethodSignature(),
					guardsPtr, guardsIdx);
		}

		if (orgMethod == 0) {
			// something went wrong with the instrumentation of this method
			Log.w(TAG, "Failed to redirect method " + from.getFullClassName() + "->" + from.getMethodName() + " to " + to.getFullClassName() + "->" + to.getMethodName());
			return 0;
		} else {
			// add the guard to guards array
			guards[guardsIdx++] = guardMethod;
		}

		Log.i(TAG, "Redirected " + from.getFullClassName() + "->" + from.getMethodName());

		return orgMethod;
    }

    private static native int redirectMethodNative(Class<?> clazz, String methodName, String prototype, int guardsPtr, int guardsIdx);
	private static native int redirectStaticMethodNative(Class<?> clazz, String methodName, String prototype, int guardsPtr, int guardsIdx);


    /**
     * Process {@link Redirect} annotations in the class specified by {@code clazz}.
     *
     * @param clazz
     */
    public static void processClass(Class<?> clazz) {
    	final Method[] methods = clazz.getDeclaredMethods();
		for (final Method method : methods) {
			if (method.isAnnotationPresent(Redirect.class)) {
				final Redirect annotation = method.getAnnotation(Redirect.class);
				final String fromDescriptor = annotation.value();
				final Signature to = Signature.fromMethod(method, clazz);

		    	final Signature[] matches = Signature.matchSignatures(fromDescriptor, to);
		    	if (matches == null) {
		    		Log.w(TAG, "Failed to redirect method " + fromDescriptor);
		    		continue;
		    	}

		    	final Signature fromSignature = matches[0];
		    	final Signature toSignature = matches[1];

		    	final String id;
		    	final Id idAnnotation = method.getAnnotation(Id.class);
		    	if (idAnnotation != null) {
		    		id = idAnnotation.value();
		    	} else {
		        	final ClassLoader[] loaders = new ClassLoader[] {
		        			Thread.currentThread().getContextClassLoader(),
		        			Instrumentation.class.getClassLoader()
		        	};
		    		id = to.getResolvableMethod(loaders).toGenericString();
		    	}

		    	// Map the guard method to its original
		    	final int orgMethod = redirectMethod(fromSignature, toSignature);
		    	originals.put(id, orgMethod);
			}
		}
    }


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     */
    public static void callVoidMethod(Class<?> caller, Object _this, Object... args) {
    	callVoidMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     */
    public static void callVoidMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	callVoidMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     */
	public static native void callVoidMethod(int handle, Object _this, Object... args);


	/**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance target of the virtual method call
     * @param args Arguments to the invoked method
	 * @return The return value of the invoked method
	 */
    public static byte callByteMethod(Class<?> caller, Object _this, Object... args) {
    	return callByteMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static byte callByteMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callByteMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native byte callByteMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static short callShortMethod(Class<?> caller, Object _this, Object... args) {
    	return callShortMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static short callShortMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callShortMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native short callShortMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static int callIntMethod(Class<?> caller, Object _this, Object... args) {
    	return callIntMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static int callIntMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callIntMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native int callIntMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static long callLongMethod(Class<?> caller, Object _this, Object... args) {
    	return callLongMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static long callLongMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callLongMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native long callLongMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static float callFloatMethod(Class<?> caller, Object _this, Object... args) {
    	return callFloatMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static float callFloatMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callFloatMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native float callFloatMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static double callDoubleMethod(Class<?> caller, Object _this, Object... args) {
    	return callDoubleMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static double callDoubleMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callDoubleMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native double callDoubleMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static boolean callBooleanMethod(Class<?> caller, Object _this, Object... args) {
    	return callBooleanMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static boolean callBooleanMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callBooleanMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native boolean callBooleanMethod(int handle, Object _this, Object... args);

    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static char callCharMethod(Class<?> caller, Object _this, Object... args) {
    	return callCharMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static char callCharMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callCharMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native char callCharMethod(int handle, Object _this, Object... args);


    /**
     * Call the original implementation of the redirected virtual method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static Object callObjectMethod(Class<?> caller, Object _this, Object... args) {
    	return callObjectMethod(caller.getEnclosingMethod().toGenericString(), _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static Object callObjectMethod(String id, Object _this, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callObjectMethod(original, _this, args);
    }

    /**
     * Call the original implementation of the redirected virtual method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param _this The instance object of the virtual method call
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native Object callObjectMethod(int handle, Object _this, Object... args);



    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     */
    public static void callStaticVoidMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	callStaticVoidMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     */
    public static void callStaticVoidMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	callStaticVoidMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     */
	public static native void callStaticVoidMethod(int handle, Class<?> clazz, Object... args);

    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static byte callStaticByteMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticByteMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static byte callStaticByteMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticByteMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native byte callStaticByteMethod(int handle, Class<?> clazz, Object... args);

    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static short callStaticShortMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticShortMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static short callStaticShortMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticShortMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
	public static native short callStaticShortMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static int callStaticIntMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticIntMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static int callStaticIntMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticIntMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native int callStaticIntMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static long callStaticLongMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticLongMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static long callStaticLongMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticLongMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native long callStaticLongMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static float callStaticFloatMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticFloatMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static float callStaticFloatMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticFloatMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native float callStaticFloatMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static double callStaticDoubleMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticDoubleMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static double callStaticDoubleMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticDoubleMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native double callStaticDoubleMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static boolean callStaticBooleanMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticBooleanMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static boolean callStaticBooleanMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticBooleanMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native boolean callStaticBooleanMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static char callStaticCharMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticCharMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static char callStaticCharMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticCharMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native char callStaticCharMethod(int handle, Class<?> clazz, Object... args);


    /**
     * Call the original implementation of the redirected static method identified by the
     * {@code caller} argument, which must be a class declared within the method being
     * redirected to (annotated with {@link Redirect}).
     *
     * @param caller A class declared in the target method of a redirection (annotated with {@link Redirect})
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static Object callStaticObjectMethod(Class<?> caller, Class<?> clazz, Object... args) {
    	return callStaticObjectMethod(caller.getEnclosingMethod().toGenericString(), clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code id}.
     *
     * @param id The identifier of the method to invoke set via the {@link Id} annotation
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static Object callStaticObjectMethod(String id, Class<?> clazz, Object... args) {
    	Integer original = originals.get(id);
    	if (original == null) {
    		throw new IllegalStateException("Could not find original method for " + id);
    	}
    	return callStaticObjectMethod(original, clazz, args);
    }

    /**
     * Call the original implementation of the redirected static method identified by {@code handle}.
     *
     * @param handle The handle identifying the method to invoke as returned by {@link Instrumentation#redirectMethod}
     * @param clazz The class where the invoked method is declared in
     * @param args Arguments to the invoked method
     * @return The return value of the invoked method
     */
    public static native Object callStaticObjectMethod(int handle, Class<?> clazz, Object... args);











    ////////////////////////////////////////////////////////////////////////////////////////
    // XXX Test
    ////////////////////////////////////////////////////////////////////////////////////////

    public static native void removeAbstractModifier(Class<?> clazz);
}
