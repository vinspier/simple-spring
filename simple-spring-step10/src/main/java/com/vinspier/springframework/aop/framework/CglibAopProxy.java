package com.vinspier.springframework.aop.framework;

import com.vinspier.springframework.aop.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 面向切面代理 抽象定义
 *
 * @author  xiaobiao.fan
 * @date    2022/10/21 11:02 上午
*/
public class CglibAopProxy implements AopProxy {

    private AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetSource().getTargetClass());
        enhancer.setInterfaces(advisedSupport.getTargetSource().getTargetInterfaces());
        enhancer.setCallback(new DynamicAdvisedMethodInterceptor(advisedSupport));
        return enhancer.create();
    }

    private static class DynamicAdvisedMethodInterceptor implements MethodInterceptor {
        private AdvisedSupport advised;

        public DynamicAdvisedMethodInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(),method,objects,methodProxy);
            // 方法规则校验
            if (advised.getMethodMatcher().matches(method,advised.getTargetSource().getTargetClass())) {
                // 进入切面 拦截增强
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            // 不进入切面
            return methodInvocation.proceed();
        }

    }

    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {

        private MethodProxy methodProxy;

        public CglibMethodInvocation(Object target, Method method, Object[] args, MethodProxy methodProxy) {
            super(target, method, args);
            this.methodProxy = methodProxy;
        }

        @Override
        public Object proceed() throws Throwable {
            return methodProxy.invoke(target,args);
        }

    }

}
