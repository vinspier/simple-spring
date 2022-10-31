package com.vinspier.biz.service.impl;

import com.vinspier.biz.service.CommonPublisherService;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.context.ApplicationContext;
import com.vinspier.springframework.context.ApplicationContextAware;
import com.vinspier.springframework.context.ApplicationEvent;
import com.vinspier.springframework.context.ApplicationEventPublisher;

public class CommonPublisherServiceImpl implements CommonPublisherService, ApplicationContextAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(ApplicationEvent event) {
        if (event == null) {
            throw new NullPointerException("event must can not be null!");
        }
        this.applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationEventPublisher = applicationContext;
    }

}
