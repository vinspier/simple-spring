package com.vinspier.springframework.beans.factory.config;

import com.sun.istack.internal.Nullable;
import com.vinspier.springframework.beans.factory.HierarchicalBeanFactory;
import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.util.StringValueResolver;

/**
 * 可配置 bean工厂
 * */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    // 单例模式
    String SCOPE_SINGLETON = "singleton";

    // 原型模式
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 注册 bean初始化增强
     * */
    void addBeanPostProcessor(BeanPostProcessor processor);

    /**
     * 销毁实例
     * */
    void destroySingletons();

    /**
     * 添加属性值表达式处理器
     * 可嵌入式的
     * */
    void addEmbeddedValueResolver(StringValueResolver resolver);

    /**
     * 处理属性值表达式 获取配置值
     * */
    String resolveEmbeddedValue(String valueExpression);

    /**
     * 设置属性转换器
     * */
    void setConvertService(ConversionService conversionService);

    /**
     * 获取属性转换器
     * */
    @Nullable
    ConversionService getConvertService();
}
