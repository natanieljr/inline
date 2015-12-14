package de.uds.infsec.instrumentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Id} annotation identifies a particular redirection. The value of
 * this annotation can be passed to the {@code call*Method} family of functions to
 * call the original implementation of the redirected method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {

	String value();

}
