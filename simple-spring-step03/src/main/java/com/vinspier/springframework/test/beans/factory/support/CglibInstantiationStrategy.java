package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

public class CglibInstantiationStrategy implements InstantiationStrategy {

    @Override
    public <T> T instantiate(BeanDefinition beanDefinition, String beanName, Constructor<T> constructor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        Object instance = null != constructor ? enhancer.create(constructor.getParameterTypes(),args) : enhancer.create();
        return (T) instance;
    }

}
