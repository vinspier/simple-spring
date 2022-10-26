package com.vinspier.springframework.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 5:56 下午
*/
public interface MethodMatcher {

    /**
     * 判定 目标方法是否符合切点定义的方法规则
     */
    boolean matches(Method method,Class<?> targetClazz);

}
