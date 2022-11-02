package com.vinspier.springframework.beans.factory;

/**
 * bean 顶层抽象
 * */
public interface BeanFactory {

    /**
     * 获取bean
     * */
    Object getBean(String name);

    /**
     * 获取bean
     * 携带 参数
     * */
    Object getBean(String name, Object... args);

    /**
     * 指定名字和类型 获取bean
     * */
    <T> T getBean(String name, Class<T> type);

    /**
     * 指定类型 获取bean
     * */
    <T> T getBean(Class<T> type);

    /**
     * 是否存在bean
     * */
    boolean containsBean(String name);

}
