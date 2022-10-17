package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.DisposableBean;
import com.vinspier.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单例行为抽象
 * */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 所有的单例bean实例
     * */
    private final Map<String,Object> singletonBeansMap = new HashMap<>();

    /**
     * 所有继承了DisposableBean接口的实例
     * */
    private final Map<String, DisposableBean> disposableBeansMap = new HashMap<>();

    @Override
    public void registrySingleton(String beanName, Object bean) {
        singletonBeansMap.put(beanName,bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonBeansMap.get(beanName);
    }

    public void registryDisposableBean(String beanName,DisposableBean disposableBean) {
        this.disposableBeansMap.put(beanName,disposableBean);
    }

    /**
     * 销毁实例 具体实现
     * */
    public void destroySingletons() {
        Set<String> disposableBeanNames = disposableBeansMap.keySet();
        disposableBeanNames.forEach(beanName -> {
            try {
                disposableBeansMap.get(beanName).destroy();
                disposableBeansMap.remove(beanName);
            } catch (Exception e) {
                throw new BeansException("execute destroy method failed on bean named : " + beanName);
            }
        });
    }

}
