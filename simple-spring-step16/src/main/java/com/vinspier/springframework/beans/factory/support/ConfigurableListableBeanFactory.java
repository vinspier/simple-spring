package com.vinspier.springframework.beans.factory.support;


import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.ListableBeanFactory;
import com.vinspier.springframework.beans.factory.config.AutowireCapableBeanFactory;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * 继承多个 不同类型 bean抽象工厂
 * 可根据 可配置工厂策略 实现对bean的类型做更改（bean的执行范围scope）
 * */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void addBeanPostProcessor(BeanPostProcessor processor);

    void preInstantiateSingletons();
}
