package com.vinspier.springframework.beans.factory;

import com.vinspier.springframework.beans.exception.BeansException;

/**
 * bean 工厂通知回调
 *
 * 触发时机：bean实例初始化阶段
 * */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
