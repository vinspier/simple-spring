package com.vinspier.springframework.context;

import com.vinspier.springframework.beans.factory.HierarchicalBeanFactory;
import com.vinspier.springframework.beans.factory.ListableBeanFactory;
import com.vinspier.springframework.core.io.ResourceLoader;

/**
 * 容器上下文
 * 继承 ListableBeanFactory 通过类型获取bean能力
 * */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader,ApplicationEventPublisher {


}
