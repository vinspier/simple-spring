package com.vinspier.springframework.aop.framework;

import com.vinspier.springframework.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 面向切面代理 抽象定义
 *
 * @author  xiaobiao.fan
 * @date    2022/10/21 11:02 上午
*/
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),advisedSupport.getTargetSource().getTargetInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 方法规则校验
        if (advisedSupport.getMethodMatcher().matches(method,advisedSupport.getTargetSource().getTargetClass())) {
            // todo 获取所有增强方法 按顺序规则执行 advisedSupport.getAdvisors()
            // 进入切面 拦截增强
            MethodInterceptor interceptor = advisedSupport.getMethodInterceptor();
            return interceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(),method,args));
        }
        // 不进入切面
        return method.invoke(advisedSupport.getTargetSource().getTarget(),args);
    }

}
