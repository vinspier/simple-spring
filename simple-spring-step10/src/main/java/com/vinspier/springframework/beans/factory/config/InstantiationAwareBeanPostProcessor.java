package com.vinspier.springframework.beans.factory.config;
/**
 * 实例级别 bean初始化通知
 * */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * bean 初始化 前置处理
     * 返回对象 可能会是一个代理对象
     * */
    Object postProcessBeforeInstantiation(Class<?> beanClass,String beanName);

}
