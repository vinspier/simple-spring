package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 抽象 实例创建、注册 工厂
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean;
        try {
            bean = createBeanInstance(beanName,beanDefinition,args);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException("create bean instance failed for bean name: " + beanName);
        }
        // *** 注册到单例 存储缓存中
        registrySingleton(beanName,bean);
        return bean;
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition,Object[] args) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Constructor<?> constructor = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        // 根据参数 匹配 合适的构造方法
        if (null != args) {
            for (Constructor<?> c : constructors) {
                if (c.getParameterTypes().length == args.length) {
                    constructor = c;
                    break;
                }
            }
        }
        return instantiationStrategy.instantiate(beanDefinition,beanName,constructor,args);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

}
