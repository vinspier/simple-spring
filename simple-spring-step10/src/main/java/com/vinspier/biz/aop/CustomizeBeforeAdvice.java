package com.vinspier.biz.aop;

import com.vinspier.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class CustomizeBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("=====================> 进入自定义方法[前置增强]逻辑");
    }

}
