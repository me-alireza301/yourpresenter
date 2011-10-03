package com.google.code.yourpresenter.util;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Similar to @PostConstruct Spring annotation, but exectuted later in
 * applicatin lefecycle, in order to get transaction management ready.
 * 
 * Found on http://forum.springsource.org/showthread.php?p=252616#post252616
 * 
 * @author AlphaCSP
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
// http://forum.springsource.org/showthread.php?67002-Proxy-not-taking-over-annotations-of-interface
@Inherited
public @interface PostInitialize {
    int order() default 0;
}
