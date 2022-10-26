package com.vinspier.springframework.beans.annotation;

import java.lang.annotation.*;

/**
 * bean注入修饰 注解
 *
 * @author  xiaobiao.fan
 * @date    2022/10/26 1:55 下午
*/

@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.TYPE,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Qualifier {

    /**
     * 指定bean名称
     * 搭配@Autowired注解，完成接口多实现的具体子类注入
     * */
    String value() default "";

}
