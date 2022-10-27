package com.vinspier.springframework.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * bean扫描支持
 *
 * @author  xiaobiao.fan
 * @date    2022/10/25 5:11 下午
*/
public class ClasspathScanningCandidateComponentProvider {

    /**
     * 找到component注解 标注的类
     * */
    public Set<BeanDefinition> findCandidateComponents(String scanPackagePath) {
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(scanPackagePath, Component.class);
        return classes.stream()
                .map(BeanDefinition::new)
                .collect(Collectors.toSet());
    }

}
