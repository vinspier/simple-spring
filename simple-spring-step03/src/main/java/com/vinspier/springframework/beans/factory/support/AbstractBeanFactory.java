package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;

/**
 * 抽象实例工厂
 * */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null, null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName,args);
    }

    /**
     * 定义 实例创建 抽象流程
     * */
    public <T> T doGetBean(final String beanName,final Object[] args) {
        Object result = getSingleton(beanName);
        if (null != result){
            return (T) result;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return (T) createBean(beanName,beanDefinition,args);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition,Object[] args);
}
