package com.vinspier.springframework.test.beans.factory.config;

import com.vinspier.springframework.test.beans.factory.HierarchicalBeanFactory;

/**
 * 可配置 bean工厂
 * */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    // 单例模式
    String SCOPE_SINGLETON = "singleton";

    // 原型模式
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor processor);

}
