package com.vinspier.springframework.test.beans.factory.config;

import com.vinspier.biz.service.StockService;

/**
 * 自定义 bean实例初始化 增强处理
 * 执行切入点 在beanFactory容器调用getBean方法时
 * 具体在 AbstractAutowireCapableBeanFactory createBean方法中完成
 * */
public class CustomizeBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("---------> simulate enhance bean before initializing 模拟前置增加实例");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("---------> simulate enhance bean before initializing 模拟后置增加实例");
        if ("stockService".equals(beanName)) {
            ((StockService) bean).setWarehouse("modify-ewe");
        }
        return bean;
    }

}
