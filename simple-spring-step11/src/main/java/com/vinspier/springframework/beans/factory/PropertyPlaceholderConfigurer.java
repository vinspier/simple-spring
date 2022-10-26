package com.vinspier.springframework.beans.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.core.io.ClasspathResourceLoader;
import com.vinspier.springframework.core.io.Resource;

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
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (value instanceof String && ((String) value).startsWith(PROP_PLACEHOLDER_PREFIX)) {
                        String strVal = (String) value;
                        int startIdx = strVal.indexOf(PROP_PLACEHOLDER_PREFIX);
                        int stopIdx = strVal.indexOf(PROP_PLACEHOLDER_SUFFIX);
                        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
                            String propKey = strVal.substring(startIdx + 2, stopIdx);
                            String propVal = properties.getProperty(propKey);
                            propertyValue.setValue(propVal);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyLocation() {
        return propertyLocation;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.propertyLocation = propertyLocation;
    }

}
