package com.vinspier.springframework.beans.factory;

/**
 * bean 名称 Aware通知回调
 *
 * 触发时机：bean实例初始化阶段
 * */
public interface BeanNameAware extends Aware {

    void setBeanName(String name);

}
