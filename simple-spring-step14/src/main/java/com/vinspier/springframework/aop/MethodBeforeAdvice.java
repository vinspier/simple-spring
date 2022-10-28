package com.vinspier.springframework.aop;

import java.lang.reflect.Method;

/**
 * 前置方法 通知
 * 具体逻辑由各自继承类实现
 *
 * @author  xiaobiao.fan
 * @date    w 2:30 下午
*/
public interface MethodBeforeAdvice extends MethodAdvice {

    /**
     * @param method 目标调用方法
     * @param args 目标调用方法参数
     * @param target 目标对象
     *
     * */
    void before(Method method,Object[] args,Object target);

}
