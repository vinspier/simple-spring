package com.vinspier.springframework.context.support;

import com.vinspier.springframework.test.beans.factory.support.BeanDefinitionRegistry;
import com.vinspier.springframework.test.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.test.beans.factory.support.DefaultListableBeanFactory;

/**
 * 可支持上下文刷新 抽象行为
 * 上下文的操作 是基于其bean容器的
 * */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 读取配置 至bean容器
     * */
    protected abstract void loadBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry);

}
