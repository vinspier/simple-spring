package com.vinspier.springframework.aop.framework.adapter;


import com.vinspier.springframework.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 前置增强方法拦截适配
 *
 * @author  xiaobiao.fan
 * @date    w 3:17 下午
*/
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private final MethodBeforeAdvice beforeAdvice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice beforeAdvice) {
        this.beforeAdvice = beforeAdvice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 执行 前置增强方法
        System.out.println("=================> 开始[前置方法]拦截器");
        beforeAdvice.before(methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        System.out.println("=================> 结束[前置方法]拦截器");
        // 执行目标方法
        return methodInvocation.proceed();
    }

}
