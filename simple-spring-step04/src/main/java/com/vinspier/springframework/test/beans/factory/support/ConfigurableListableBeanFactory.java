package com.vinspier.springframework.test.beans.factory.support;


import com.vinspier.springframework.test.beans.factory.ListableBeanFactory;
import com.vinspier.springframework.test.beans.factory.config.AutowireCapableBeanFactory;
import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.test.beans.factory.config.ConfigurableBeanFactory;

/**
 * 继承多个 不同类型 bean抽象工厂
 * 可根据 可配置工厂策略 实现对bean的类型做更改（bean的执行范围scope）
 * */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

}
