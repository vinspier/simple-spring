package com.vinspier.springframework.beans.annotation;

import java.lang.annotation.*;

/**
 * bean依赖注入 注解
 * 以bean类型方式注入 可搭配Qualifier注解 标注bean名称
 * @author  xiaobiao.fan
 * @date    2022/10/26 1:55 下午
*/

@Target({ElementType.CONSTRUCTOR,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

}
