package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;

import java.util.Set;

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

    /**
     * 判断bean元数据定义 已经存在
     * */
    boolean containsBeanDefinition(String beanName);

    Set<String> getBeanDefinitionNames();
}
