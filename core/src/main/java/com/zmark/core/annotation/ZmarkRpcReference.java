package com.zmark.core.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhengguangchen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Autowired
public @interface ZmarkRpcReference {
    long timeout() default 1000;
}
