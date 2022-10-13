package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;

/**
 * 抽象 实例创建、注册 工厂
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;
        try {
            // 创建实例 可在这里指定策略 （jdk/cglib）
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new BeansException("create bean instance failed for bean name " + beanName);
        }
        // *** 注册到单例 存储缓存中
        registrySingleton(beanName,bean);
        return bean;
    }

}
