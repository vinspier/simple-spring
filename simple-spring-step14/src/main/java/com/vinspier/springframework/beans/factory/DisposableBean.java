package com.vinspier.springframework.beans.factory;

/**
 * bean销毁 自定义行为
 * 应该被具体的bean实例 去实现
 * */
public interface DisposableBean {

    String DESTROY_METHOD_NAME = "destroy";

    /**
     * 销毁
     * 时机：容器被关闭shutdown时触发
     * */
    void destroy() throws Exception;

}
