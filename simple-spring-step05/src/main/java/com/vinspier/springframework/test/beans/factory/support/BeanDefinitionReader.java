package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.core.io.Resource;
import com.vinspier.springframework.core.io.ResourceLoader;

import java.util.List;
import java.util.Set;

/**
 * bean元数据 读取行为定义
 * */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getBeanDefinitionRegistry();

    ResourceLoader getResourceLoader();

    /**
     * 指定路径获取资源 并解析元数据
     * */
    void loadBeanDefinitions(String location);

    /**
     * 指定路径获取资源 并解析元数据
     * */
    void loadBeanDefinitions(Set<String> locations);

    /**
     * 资源文件中获取元数据
     * */
    void loadBeanDefinitions(Resource resource);

    /**
     * 多个资源文件中获取元数据
     * */
    void loadBeanDefinitions(List<Resource> resources);


}
