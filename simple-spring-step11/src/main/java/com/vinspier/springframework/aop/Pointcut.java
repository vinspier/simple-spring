package com.vinspier.springframework.aop;

/**
 * 切点抽象
 * 由类过滤器和方法匹配器组成 两者同等重要
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 5:52 下午
*/
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
