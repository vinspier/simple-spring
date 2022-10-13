package com.vinspier.springframework.test.beans.factory.config;

/**
 * 单例行为抽象
 * */
public interface SingletonBeanRegistry {

    /**
     * 注册单例
     * */
    void registrySingleton(String beanName,Object bean);

    /**
     * 获取单例
     * */
    Object getSingleton(String beanName);

}
