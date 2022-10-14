package com.vinspier.springframework.beans.factory;

import java.util.Map;
import java.util.Set;

/**
 * bean获取 拓展行为
 * 类型方式
 * */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * 通过指定类型 获取bean实例
     * */
    <T> Map<String,T> getBeansOfType(Class<?> clazz);

    Set<String> getBeanDefinitionNames();

}
