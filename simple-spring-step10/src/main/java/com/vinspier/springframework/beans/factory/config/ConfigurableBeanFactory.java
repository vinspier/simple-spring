package com.vinspier.springframework.beans.factory.config;

import com.vinspier.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * 可配置 bean工厂
 * */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    // 单例模式
    String SCOPE_SINGLETON = "singleton";

    // 原型模式
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 注册 bean初始化增强
     * */
    void addBeanPostProcessor(BeanPostProcessor processor);

    /**
     * 销毁实例
     * */
    void destroySingletons();

}
