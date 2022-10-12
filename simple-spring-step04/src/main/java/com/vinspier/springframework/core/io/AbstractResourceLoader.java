package com.vinspier.springframework.core.io;

import com.vinspier.springframework.util.StringUtils;

/**
 * 资源加载 抽象行为
 * */
public abstract class AbstractResourceLoader implements ResourceLoader {

    @Override
    public Resource loadResource(String location) {
        if (StringUtils.isEmpty(location)) {
            throw new IllegalArgumentException("resource fetch target location should not be empty!");
        }
        return null;
    }
}
