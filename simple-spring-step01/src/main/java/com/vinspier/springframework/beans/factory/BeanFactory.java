package com.vinspier.springframework.beans.factory;

/**
 * bean 顶层抽象
 * */
public interface BeanFactory {

    /**
     * 获取实例
     * */
    Object getBean(String beanName);

}
