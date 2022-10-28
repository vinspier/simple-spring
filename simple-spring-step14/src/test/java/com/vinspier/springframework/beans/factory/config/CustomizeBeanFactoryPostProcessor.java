package com.vinspier.springframework.beans.factory.config;

import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;

/**
 * 自定义 bean工厂增强处理
 * */
public class CustomizeBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        System.out.println("---------------------------> 执行beanFactory容器资源加载完毕后的增强行为 进入用户自定义增加方法");
        // 对一些bean的定义 进行修改等功能（决定于beanFactory具备哪些能力）
    }

}
