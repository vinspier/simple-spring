package com.vinspier.springframework.beans.factory;


import com.vinspier.biz.model.CommonEvent;
import com.vinspier.biz.service.CommonPublisherService;
import com.vinspier.springframework.context.support.ClasspathApplicationContext;
import org.junit.Test;

public class ApplicationEventTest {

    @Test
    public void testEvent() {
        String configLocation = "classpath:spring.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        CommonPublisherService commonPublisherService = (CommonPublisherService) applicationContext.getBean("commonPublisherService");
        commonPublisherService.publish(new CommonEvent(1,"库存修改","{'afterValue':1000}"));
        applicationContext.registryShutdownHook();
    }

}