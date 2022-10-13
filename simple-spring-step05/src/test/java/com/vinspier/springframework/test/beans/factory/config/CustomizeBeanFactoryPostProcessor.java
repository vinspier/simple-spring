package com.vinspier.springframework.test.beans.factory.config;

import com.vinspier.springframework.test.beans.factory.support.ConfigurableListableBeanFactory;

/**
 * 自定义 bean工厂增强处理
 * */
public class CustomizeBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        System.out.println("---------> beanFactory加载元数据完成 进入用户自定义增加方法");
        // 对一些bean的定义 进行修改等功能（决定于beanFactory具备哪些能力）
    }

}
