package com.vinspier.springframework.context.support;

import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.context.ApplicationContext;
import com.vinspier.springframework.context.ApplicationContextAware;

/**
 * 上下文容器 准备完毕通知 回调
 * */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    /**
     * 在抽象层上下文容器实现类AbstractApplicationContext初始化时
     * 第一个注册该BeanPostProcessor
     * */
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
