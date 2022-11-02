package com.vinspier.springframework.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.util.LinkedList;
import java.util.List;

/**
 * 面向切面动态代理的基础配置管理
 *
 * @author  xiaobiao.fan
 * @date    2022/10/21 10:58 上午
*/
public class AdvisedSupport {

    // 代理类型 可做配置项
    private boolean cglibProxyType = true;

    // 被代理目标对象
    private TargetSource targetSource;

    // 方法拦截器
    private MethodInterceptor methodInterceptor;

    // 方法规则校验器
    private MethodMatcher methodMatcher;

    // todo 方法aop增强 供代理对象回调使用
    private List<Advisor> advisors = new LinkedList<>();

    public AdvisedSupport() {

    }

    public AdvisedSupport(TargetSource targetSource, MethodInterceptor methodInterceptor, MethodMatcher methodMatcher) {
        this.targetSource = targetSource;
        this.methodInterceptor = methodInterceptor;
        this.methodMatcher = methodMatcher;
    }

    public void setCglibProxyType(boolean cglibProxyType) {
        this.cglibProxyType = cglibProxyType;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public boolean isCglibProxyType() {
        return cglibProxyType;
    }

    public List<Advisor> getAdvisors() {
        return advisors;
    }

    public void addAdvisor(Advisor advisor) {
        this.advisors.add(advisor);
    }

    public void setAdvisors(List<Advisor> advisors) {
        this.advisors = advisors;
    }
}
