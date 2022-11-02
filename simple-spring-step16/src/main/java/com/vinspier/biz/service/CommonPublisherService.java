package com.vinspier.biz.service;

import com.vinspier.springframework.context.ApplicationEvent;

public interface CommonPublisherService {

    void publish(ApplicationEvent event);

}
