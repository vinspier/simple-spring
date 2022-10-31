package com.vinspier.springframework.core.io;

/**
 * 资源加载 行为定义
 * */
public interface ResourceLoader {

    String CLASSPATH_PREFIX = "classpath:";

    String CLASSPATH_PREFIX_ALL = "classpath:*";

    String HTTP_PREFIX = "http";

    String HTTPS_PREFIX = "https";

    /**
     * 加载资源
     * */
    Resource loadResource(String location);

}
