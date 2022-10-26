package com.vinspier.springframework.beans.factory.xml;

/**
 * xml形式bean配置标签 定义
 * */
public enum XmlTagEnum {

    COMPONENT_SCAN("component-scan"),
    BASE_PACKAGE("base-package"),
    BEANS("beans"),
    BEAN("bean"),
    ID("id"),
    NAME("name"),
    VALUE("value"),
    CLASS_TAG("class"),
    INIT_METHOD("init-method"),
    DESTROY_METHOD("destroy-method"),
    PROPERTY("property"),
    REF("ref"),
    SCOPE("scope");

    private final String tag;

    XmlTagEnum(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }

}
