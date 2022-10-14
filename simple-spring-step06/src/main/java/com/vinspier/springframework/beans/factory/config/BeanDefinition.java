package com.vinspier.springframework.beans.factory.config;

import com.vinspier.springframework.beans.PropertyValues;

/**
 * 实例元数据定义内容
 * */
public class BeanDefinition {

    private Class<?> beanClass;

    /**
     * 实例元数据 属性定义
     * */
    private PropertyValues propertyValues;

    /**
     * 自定义配置 初始逻辑方法
     * */
    private String initMethodName;

    /**
     * 自定义配置 销毁逻辑方法
     * */
    private String destroyMethodName;

    public BeanDefinition(Class beanClass) {
        this(beanClass,new PropertyValues());
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = null != propertyValues ? propertyValues : new PropertyValues();
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }
}
