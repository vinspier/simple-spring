package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry,ConfigurableListableBeanFactory {

    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (null == beanDefinition) {
            throw new BeansException("no bean named [" + beanName + "] is defined !" );
        }
        return beanDefinition;
    }

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
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

    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.forEach((beanName,beanDefinition) -> {
            getBean(beanName);
        });
    }

    @Override
    public void destroySingletons() {
        super.destroySingletons();
    }

    @Override
    public <T> T getBean(Class<T> type) {
        List<String> beanNames = new LinkedList<>();
        beanDefinitionMap.forEach((k,v) -> {
            if (type.isAssignableFrom(v.getBeanClass())) {
                beanNames.add(k);
            }
        });
        if (beanNames.size() != 1) {
            throw new BeansException(String.format("class named %s expected single bean but found more than one",type.getSimpleName()));
        }
        return getBean(beanNames.get(0),type);
    }

}
