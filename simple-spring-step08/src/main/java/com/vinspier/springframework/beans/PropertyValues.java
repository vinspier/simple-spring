package com.vinspier.springframework.beans;

import java.util.LinkedList;
import java.util.List;

/**
 * 属性管理
 * */
public class PropertyValues {

    private List<PropertyValue> propertyValues = new LinkedList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }

    public PropertyValue getPropertyValue(String propertyName) {
       return propertyValues.stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst()
                .orElse(null);
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

}
