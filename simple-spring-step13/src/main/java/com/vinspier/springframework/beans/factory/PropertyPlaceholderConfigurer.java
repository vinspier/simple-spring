package com.vinspier.springframework.beans.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.core.io.ClasspathResourceLoader;
import com.vinspier.springframework.core.io.Resource;
import com.vinspier.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * 属性预占符 配置实现
 *
 * @author  xiaobiao.fan
 * @date    2022/10/25 5:58 下午
*/
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private static final String PROP_PLACEHOLDER_PREFIX = "${";

    private static final String PROP_PLACEHOLDER_SUFFIX = "}";

    // 属性配置路径
    private String propertyLocation;

    /**
     * 读取classpath路径下 属性配置 填充到beanDefinition中去
     * */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
        Resource resource = resourceLoader.loadResource(propertyLocation);
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            Set<String> definitionNames = beanFactory.getBeanDefinitionNames();
            for (String name : definitionNames) {
                BeanDefinition definition = beanFactory.getBeanDefinition(name);
                PropertyValues propertyValues = definition.getPropertyValues();
                if (CollectionUtil.isEmpty(properties)) {
                    continue;
                }
                // *** tips: 保留xml配置bean的属性配置方式注入${config_filed_name.name1.xxx}
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (value instanceof String && ((String) value).startsWith(PROP_PLACEHOLDER_PREFIX)) {
                        String valueExpression = (String) value;
                        propertyValue.setValue(resolvePlaceholderValue(valueExpression,properties));
                    }
                }
            }
            // *** tips: 为beanFactory容器提供配置文件值，提供注解式属性注入
            // 在 AutowiredAnnotationBeanPostProcessor 中解析@Value注解时 回调处理使用
            StringValueResolver resolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(resolver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String resolvePlaceholderValue(String valueExpression,Properties properties) {
        int startIdx = valueExpression.indexOf(PROP_PLACEHOLDER_PREFIX);
        int stopIdx = valueExpression.indexOf(PROP_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String propKey = valueExpression.substring(startIdx + 2, stopIdx);
            return properties.getProperty(propKey);
        }
        return null;
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

    /**
     * 配置文件属性值处理器
     * config_files: xxx.properties
     * */
    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolve(String valueExpression) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholderValue(valueExpression,properties);
        }

    }

}
