package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.test.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例行为抽象
 * */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private Map<String,Object> singletonBeansMap = new HashMap<>();

    @Override
    public void registrySingleton(String beanName, Object bean) {
        singletonBeansMap.put(beanName,bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonBeansMap.get(beanName);
    }

}
