package com.vinspier.springframework.beans.factory.config;

import com.vinspier.springframework.beans.PropertyValues;

/**
 * 实例级别 bean初始化通知
 * */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * bean 初始化 前置处理
     * 返回对象 可能会是一个代理对象（移动至 postProcessAfterInstantiation方法做动态对象创建）
     * */
    Object postProcessBeforeInstantiation(Class<?> beanClass,String beanName);

    /**
     * bean 初始化 后置处理
     * 返回boolean类型 true 继续处理，false 结束处理直接返回bean实例给调用者
     * */
    default boolean postProcessAfterInstantiation(Object bean,String beanName) {
        return true;
    }

    /**
     * 处理属性的注解式注入
     * @Autowired @Qualifier @Value
     * 时机：在bean实例化之后 在bean统一填充beanDefinition属性之前
     * */
     default PropertyValues postProcessPropertyValues(PropertyValues pvs,Object bean,String beanName) {
         return null;
     }

}
