package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.factory.config.BeanDefinition;

/**
 * bean定义 抽象行为
 * */
public interface BeanDefinitionRegistry {

    /**
     * 获取bean定义元数据
     * */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 注册bean定义元数据
     * */
    void registryBeanDefinition(String beanName,BeanDefinition beanDefinition);

}
