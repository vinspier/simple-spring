package com.vinspier.springframework.beans.annotation;

import java.lang.annotation.*;

/**
 * bean属性 配置文件 注入注解
 *
 * @author  xiaobiao.fan
 * @date    2022/10/26 1:55 下午
*/
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * 属性配置表达式: ${config_filed_name.name2.xxx}
     * */
    String value();

}
