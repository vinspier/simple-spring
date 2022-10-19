package com.vinspier.springframework.beans.factory;

/**
 * bean初始化 自定义行为定义
 * 应该被具体的bean实例 去实现
 * */
public interface InitializingBean {

    String INITIALIZE_METHOD_NAME = "afterPropertiesSet";

    /**
     * 初始化
     * 时机：
     * 1、在beanFactory对bean填充完原始属性之后
     * 2、在beanPostProcessor对初始化 前置增强之后
     * 3、在bean配置 自定义配置初始化方法之前
     * 4、在beanPostProcessor对初始化 后置增强之钱
     * */
    void afterPropertiesSet() throws Exception;

}
