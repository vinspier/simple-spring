package com.vinspier.springframework.test.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry,ConfigurableListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (null == beanDefinition) {
            throw new BeansException("no bean named "+ beanName + " is defined !" );
        }
        return beanDefinition;
    }

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<?> clazz) {
        Map<String, T> beansTypeMap = new HashMap<>();
        beanDefinitionMap.forEach((beanName,beanDefinition) -> {
            Class<?> clazz1 = beanDefinition.getBeanClass();
            if (clazz.isAssignableFrom(clazz1)) {
                beansTypeMap.put(beanName,(T) getBean(beanName));
            }
        });
        return beansTypeMap;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return beanDefinitionMap.keySet();
    }
}
