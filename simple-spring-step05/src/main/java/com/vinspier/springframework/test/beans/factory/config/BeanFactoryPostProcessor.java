package com.vinspier.springframework.test.beans.factory.config;

import com.vinspier.springframework.test.beans.factory.support.ConfigurableListableBeanFactory;

/**
 * 容器级别
 * bean容器 处理器
 * 允许自定义修改 beanDefinition元数据内容
 *
 * 执行切入点 在上下文容器AbstractApplicationContext中
 * 获取到加载完元数据定义后的beanFactory执行
 *
 * */
public interface BeanFactoryPostProcessor {

    /**
     * beanDefinition加载完毕之后
     * bean实例初始化之前
     * ConfigurableListableBeanFactory 继承通过类型获取beanDefinition能力
     * */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

}
