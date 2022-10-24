package com.vinspier.springframework.beans.factory.config;
/**
 * 实例级别
 * bean初始化 增加处理器
 * 允许自定义修改实例初始化 前后 的bean对象
 *
 * 执行切入点 在beanFactory容器调用getBean方法时
 * 具体在 AbstractAutowireCapableBeanFactory createBean方法中完成
 * */
public interface BeanPostProcessor {

    /**
     * bean 实例化 前置处理
     * */
    Object postProcessBeforeInitialization(Object bean,String beanName);

    /**
     * bean 实例化 前置处理
     * */
    Object postProcessAfterInitialization(Object bean,String beanName);

}
