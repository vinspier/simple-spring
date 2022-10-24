package com.vinspier.springframework.context.support;

import com.vinspier.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.vinspier.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import java.util.Set;

/**
 * xml配置形式 上下文行为抽象
 *
 * */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    @Override
    protected void loadBeanDefinitions(BeanDefinitionRegistry beanDefinitionRegistry) {
        // *** tips: 本身继承了 资源加载抽象能力 AbstractResourceLoader
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanDefinitionRegistry,this);
        Set<String> locations = getConfigLocations();
        xmlBeanDefinitionReader.loadBeanDefinitions(locations);
    }

    /**
     * 定义配置资源路径
     */
    protected abstract Set<String> getConfigLocations();

}
