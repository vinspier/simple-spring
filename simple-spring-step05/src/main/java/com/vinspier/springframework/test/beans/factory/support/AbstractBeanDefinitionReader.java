package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.core.io.ClasspathResourceLoader;
import com.vinspier.springframework.core.io.ResourceLoader;

/**
 * bean元数据加载抽象行为
 * */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    // 加载bean配置的存储容器
    private BeanDefinitionRegistry registry;

    // 资源加载策略
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry,new ClasspathResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return this.registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

}
