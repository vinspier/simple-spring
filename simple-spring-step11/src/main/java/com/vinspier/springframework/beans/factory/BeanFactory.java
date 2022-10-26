package com.vinspier.springframework.beans.factory;

/**
 * bean 顶层抽象
 * */
public interface BeanFactory {

    /**
     * 获取实例
     * */
    Object getBean(String name);

    /**
     * 获取实例
     * 携带 参数
     * */
    Object getBean(String name, Object... args);

    /**
     * 指定类型 获取
     * */
    <T> T getBean(String name, Class<T> type);

}
