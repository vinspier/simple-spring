package com.vinspier.springframework.test.beans.factory.xml;

import cn.hutool.core.util.XmlUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.test.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.test.beans.factory.config.BeanReference;
import com.vinspier.springframework.test.beans.factory.support.AbstractBeanDefinitionReader;
import com.vinspier.springframework.test.beans.factory.support.BeanDefinitionRegistry;
import com.vinspier.springframework.core.io.Resource;
import com.vinspier.springframework.core.io.ResourceLoader;
import com.vinspier.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.loadResource(location);
        this.loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Set<String> locations) {
        ResourceLoader resourceLoader = getResourceLoader();
        locations.forEach(location -> {
            Resource resource = resourceLoader.loadResource(location);
            this.loadBeanDefinitions(resource);
        });
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            InputStream inputStream = resource.getInputStream();
            doLoadBeanDefinitions(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException("load bean definitions failed by parse xml document of path: " + resource.getResourceOrigin());
        }
    }

    @Override
    public void loadBeanDefinitions(List<Resource> resources) {
        resources.forEach(this::loadBeanDefinitions);
    }

    /**
     * 加载 bean元数据定义 xml文件
     * 此处可拆解出 文档解析器
     * */
    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        // todo 读取数据源 解析出bean定义
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (! (node instanceof Element) || !"bean".equals(node.getNodeName())) {
                continue;
            }
            doParseElementAndRegistry((Element) node);
        }
    }

    /**
     * 解析节点 bean定义
     * 包含属性
     * */
    protected void doParseElementAndRegistry(Element element) throws ClassNotFoundException {
        String id = element.getAttribute("id");
        String name = element.getAttribute("name");
        String className = element.getAttribute("class");
        // 类对象
        Class<?> beanClazz = Class.forName(className);
        // bean名称
        String beanName = StringUtils.isNotEmpty(id) ? id : name;
        if (StringUtils.isEmpty(name)) {
            beanName = beanClazz.getSimpleName();
        }
        BeanDefinition beanDefinition = new BeanDefinition(beanClazz);
        // 解析属性
        NodeList propertyNodes = element.getChildNodes();
        for (int i = 0; i < propertyNodes.getLength(); i++) {
            Node node = propertyNodes.item(i);
            if (! (node instanceof Element) || !"property".equals(node.getNodeName())) {
                continue;
            }
            Element propertyEle = (Element) node;
            String attrName = propertyEle.getAttribute("name");
            String attrValue = propertyEle.getAttribute("value");
            // 对象引用
            String ref = propertyEle.getAttribute("ref");
            Object object = StringUtils.isEmpty(ref) ? attrValue : new BeanReference(ref);
            PropertyValue propertyValue = new PropertyValue(attrName,object);
            beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
        }
        // bean元数据定义 重复校验
        if (getBeanDefinitionRegistry().containsBeanDefinition(beanName)) {
            throw new BeansException("beanDefinition is duplicated which bean name is " + beanName);
        }
        // 注册bean元数据定义
        getBeanDefinitionRegistry().registryBeanDefinition(beanName,beanDefinition);
    }

}
