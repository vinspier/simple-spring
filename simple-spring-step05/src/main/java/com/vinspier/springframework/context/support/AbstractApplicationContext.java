package com.vinspier.springframework.context.support;

import com.vinspier.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.context.ConfigurableApplicationContext;
import com.vinspier.springframework.core.io.AbstractResourceLoader;

import java.util.Map;
import java.util.Set;

/**
 * 上下文 抽象行为定义
 * */
public abstract class AbstractApplicationContext extends AbstractResourceLoader implements ConfigurableApplicationContext {

    /**
     * 定义刷新容器 模版方法
     * 具体行为 由实现者完成
     * */
    @Override
    public void refresh() {
        // 1、刷新 创建新容器
        refreshBeanFactory();
        // 2、获取容器
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 3、容器级别 bean实例化前置处理
        invokeBeanFactoryPostProcessors(beanFactory);
        // 4、注册 实例级别 增强处理 tips: *** 提前于其他bean
        registryBeanPostProcessors(beanFactory);
        // 5、提前实例化bean单例
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 刷新 创建新容器
     * */
    protected abstract void refreshBeanFactory();

    /**
     * 获取容器
     * */
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beanFactoryPostProcessorMap.forEach((beanName,processor) -> {
            processor.postProcessBeanFactory(beanFactory);
        });
    }

    protected void registryBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.forEach((beanName,processor) -> {
            System.out.println("[" + processor.getClass().getSimpleName() + "]" + "bean processor has been registry.");
            beanFactory.addBeanPostProcessor(processor);
        });
    }

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName,args);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<?> clazz) {
        return getBeanFactory().getBeansOfType(clazz);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
