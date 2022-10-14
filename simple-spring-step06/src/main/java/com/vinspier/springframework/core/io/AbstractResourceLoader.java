package com.vinspier.springframework.core.io;

import com.vinspier.springframework.util.StringUtils;

/**
 * 资源加载 抽象行为
 * */
public abstract class AbstractResourceLoader implements ResourceLoader {

    /**
     * 默认采用 ClasspathResourceLoader
     * */
    @Override
    public Resource loadResource(String location) {
        if (StringUtils.isEmpty(location)) {
            throw new IllegalArgumentException("resource fetch target location should not be empty!");
        }
        if (location.startsWith(CLASSPATH_PREFIX)) {
            return new ClasspathResourceLoader().loadResource(location);
        } else if (location.startsWith(HTTP_PREFIX) || location.startsWith(HTTPS_PREFIX)) {
            return new URLResourceLoader().loadResource(location);
        } else {
            return new FileSystemResourceLoader().loadResource(location);
        }
    }
}
