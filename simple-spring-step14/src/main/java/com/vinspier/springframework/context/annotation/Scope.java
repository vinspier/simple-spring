package com.vinspier.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * bean 实例范围类型
 * */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    String value() default "";

}
