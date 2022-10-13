package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.test.beans.factory.BeanFactory;
import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;

/**
 * 抽象实例工厂
 * */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 定义 实例创建 抽象流程
     * */
    @Override
    public Object getBean(String beanName) {
        Object result = getSingleton(beanName);
        if (null != result){
            return result;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanName,beanDefinition);
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition);
}
