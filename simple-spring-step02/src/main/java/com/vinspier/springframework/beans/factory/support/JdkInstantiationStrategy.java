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
                instance = constructor.newInstance(args);
            }  else {
                instance = clazz.newInstance();
            }
        } catch (Exception e) {
            throw new BeansException("initiate bean failed for bean name: "+ beanName);
        }

        return (T) instance;
    }

}
