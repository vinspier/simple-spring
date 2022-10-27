package com.vinspier.biz.service.impl;

import com.vinspier.biz.model.CommonEvent;
import com.vinspier.biz.service.CommonPublisherService;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.context.*;

public class CommonListenerServiceImpl implements ApplicationListener<CommonEvent>{

    @Override
    public void onApplicationEvent(CommonEvent event) {
        System.out.println("========================> 接收处理自定义事件消息内容: " + event.toString());
    }

}
