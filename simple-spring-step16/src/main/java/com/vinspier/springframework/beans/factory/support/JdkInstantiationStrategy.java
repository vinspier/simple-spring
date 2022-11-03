package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public class JdkInstantiationStrategy implements InstantiationStrategy {

    @Override
    public <T> T instantiate(BeanDefinition beanDefinition, String beanName, Constructor<T> constructor, Object[] args) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Object instance = null;
        try {
            if (constructor != null) {
                instance = clazz.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
            }  else {
                instance = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new BeansException("instantiate bean failed for bean name: " + beanName,e);
        }

        return (T) instance;
    }

}
