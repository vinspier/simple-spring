package com.vinspier.springframework.context.support;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 项目路径配置读取资源方式 上下文
 *
 * */
public class ClasspathApplicationContext extends AbstractXmlApplicationContext {

    private Set<String> configLocations = new HashSet<>();

    public ClasspathApplicationContext() {

    }

    public ClasspathApplicationContext(String configLocation) {
        this(new HashSet<>(Collections.singletonList(configLocation)));
    }

    public ClasspathApplicationContext(Set<String> configLocations) {
        this.configLocations = configLocations;
        refresh();
    }

    @Override
    protected Set<String> getConfigLocations() {
        return configLocations;
    }



}
