package com.vinspier.springframework.test.beans.factory.config;

import com.vinspier.springframework.beans.PropertyValues;

/**
 * 实例元数据定义内容
 * */
public class BeanDefinition {

    private Class beanClass;

    /**
     * 实例元数据 属性定义
     * */
    private PropertyValues propertyValues;

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
}
