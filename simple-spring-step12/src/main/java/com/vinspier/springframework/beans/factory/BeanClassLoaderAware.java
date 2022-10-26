package com.vinspier.springframework.beans.factory;

/**
 * bean 工厂通知回调
 *
 * 触发时机：bean实例初始化阶段
 * */
public interface BeanClassLoaderAware extends Aware {

    void setBeanClassLoader(ClassLoader classLoader);

}
