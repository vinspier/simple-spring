package com.vinspier.springframework.context;

/**
 * spi 提供可配置上下文
 * 具体功能由其实现者拓展
 * */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     * */
    void refresh();

    /**
     * 关闭容器
     * */
    void close();

    /**
     * 关闭注册bean回调钩子
     * */
    void registryShutdownHook();

}
