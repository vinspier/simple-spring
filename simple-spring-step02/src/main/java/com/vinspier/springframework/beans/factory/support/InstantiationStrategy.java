package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化策略
 * */
public interface InstantiationStrategy {

    <T> T instantiate(BeanDefinition beanDefinition, String beanName, Constructor<T> constructor,Object[] args);

}
