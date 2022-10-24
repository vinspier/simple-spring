package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.FactoryBean;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂bean拓展
 * 给未显示配置为容器管理bean，但可以从继承了FactoryBean接口的中得到的实例
 * 注册到容器中去 FactoryBeanRegistrySupport容器
 * */
public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Object NULL_OBJECT = new Object();

    private final ConcurrentHashMap<String,Object> factoryBeanMap = new ConcurrentHashMap<>();

    protected Object getCachedObjectFromFactoryBean(String name) {
        Object object = this.factoryBeanMap.get(name);
        return object == NULL_OBJECT ? null : object;
    }

    protected <T> Object getObjectFromFactoryBean(FactoryBean<T> factoryBean, String name) {
        if (factoryBean.isSingleton()) {
            Object object = this.factoryBeanMap.get(name);
            if (object == null) {
                object = this.doGetObjectFromFactoryBean(factoryBean,name);
                this.factoryBeanMap.put(name,null == object ? NULL_OBJECT : object);
            }
            return object == NULL_OBJECT ? null : object;
        } else {
            return this.doGetObjectFromFactoryBean(factoryBean,name);
        }
    }

    protected <T> Object doGetObjectFromFactoryBean(FactoryBean<T> factoryBean, String name) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("get factoryBean failed on bean named : " + name);
        }
    }

}
