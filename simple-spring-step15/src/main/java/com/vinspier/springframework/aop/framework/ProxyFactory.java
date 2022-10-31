package com.vinspier.springframework.aop.framework;

import com.vinspier.springframework.aop.AdvisedSupport;

/**
 * 代理对象生成 代理工厂
 *
 * @author  xiaobiao.fan
 * @date    2022/10/24 3:36 下午
*/
public class ProxyFactory {

    private final AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object proxy() {
        return this.createProxy().getProxy();
    }

    private AopProxy createProxy() {
        return advisedSupport.isCglibProxyType() ? new CglibAopProxy(advisedSupport) : new JdkDynamicAopProxy(advisedSupport);
    }

    public static Object proxy(AdvisedSupport advisedSupport) {
        return createProxy(advisedSupport).getProxy();
    }

    private static AopProxy createProxy(AdvisedSupport advisedSupport) {
        return advisedSupport.isCglibProxyType() ? new CglibAopProxy(advisedSupport) : new JdkDynamicAopProxy(advisedSupport);
    }

}
