package com.vinspier.biz.model;

import com.vinspier.springframework.context.ApplicationEvent;

public class CommonEvent extends ApplicationEvent {

    private Integer eventType;

    private String desc;

    private Object data;

    public CommonEvent(Integer eventType, String desc, Object data) {
        super(eventType);
        this.eventType = eventType;
        this.desc = desc;
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonEvent{" +
                "eventType=" + eventType +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }

}
