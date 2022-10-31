package com.vinspier.springframework.context.annotation;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.vinspier.springframework.beans.annotation.AutowiredAnnotationBeanPostProcessor;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.vinspier.springframework.stereotype.Component;
import com.vinspier.springframework.util.StringUtils;

import java.util.Set;

/**
 * bean定义扫描实现
 *
 * @author  xiaobiao.fan
 * @date    2022/10/25 5:07 下午
*/
public class ClasspathBeanDefinitionScanner extends ClasspathScanningCandidateComponentProvider {

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public ClasspathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    /**
     * 逐个路径扫描
     * */
    public void scan(String[] scanPackagePaths) {
        for (String scanPackagePath : scanPackagePaths) {
            Set<BeanDefinition> candidateBeanDefinitions = super.findCandidateComponents(scanPackagePath);
            if (CollectionUtil.isEmpty(candidateBeanDefinitions)) {
                continue;
            }
            for (BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
                String scope = resolveBeanScope(candidateBeanDefinition);
                if (StringUtils.isNotEmpty(scope)) {
                    candidateBeanDefinition.setScope(scope);
                }
                String determinedName = resolveDeterminedBeanName(candidateBeanDefinition);
                beanDefinitionRegistry.registryBeanDefinition(determinedName,candidateBeanDefinition);
            }
        }
        // *** 注册 注解式配置bean的增强处理器 (至关重要)
        beanDefinitionRegistry.registryBeanDefinition("internalAutowiredAnnotationBeanPostProcessor",new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Scope scope = clazz.getAnnotation(Scope.class);
        if (null != scope) {
            // todo 校验值合法性
            return scope.value();
        }
        return null;
    }

    private String resolveDeterminedBeanName(BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Component component = clazz.getAnnotation(Component.class);
        String value = component.value();
        if (StringUtils.isNotEmpty(value)) {
             return value;
        }
        return StrUtil.lowerFirst(clazz.getSimpleName());
    }

}
