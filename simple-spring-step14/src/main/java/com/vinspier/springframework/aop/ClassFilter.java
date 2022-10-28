package com.vinspier.springframework.aop;

/**
 * 类过滤器
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 5:54 下午
*/
public interface ClassFilter {

    /**
     * 判定 目标类是否符合切点定义的类规则
     */
    boolean matches(Class<?> clazz);

}
