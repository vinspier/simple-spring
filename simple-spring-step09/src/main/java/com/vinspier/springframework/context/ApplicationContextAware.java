package com.vinspier.springframework.context;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.Aware;

/**
 * 上下文容器通知
 *
 * */
public interface ApplicationContextAware extends Aware {

    /**
     * 由ApplicationContextAwareProcessor处理触发执行
     * */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
