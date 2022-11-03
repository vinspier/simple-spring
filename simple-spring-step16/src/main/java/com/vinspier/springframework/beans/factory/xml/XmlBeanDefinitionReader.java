package com.vinspier.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanReference;
import com.vinspier.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.vinspier.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.vinspier.springframework.context.annotation.ClasspathBeanDefinitionScanner;
import com.vinspier.springframework.core.io.Resource;
import com.vinspier.springframework.core.io.ResourceLoader;
import com.vinspier.springframework.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        // 读取数据源 解析出bean定义
        Document doc = reader.read(inputStream);
        Element root = doc.getRootElement();
        // 1、解析xml定义的bean
        List<Element> beanElements = root.elements(XmlTagEnum.BEAN.tag());
        doParseElements(beanElements);
        // 2、解析扫描包下的bean
        Element scanElement = root.element(XmlTagEnum.COMPONENT_SCAN.tag());
        doParseComponentScan(scanElement);
    }

    protected void doParseComponentScan(Element scanElement) {
        if (null == scanElement) {
            return;
        }
        String scanPackagePath = scanElement.attributeValue(XmlTagEnum.BASE_PACKAGE.tag());
        if (StringUtils.isEmpty(scanPackagePath)) {
            throw new BeansException("component scan package attribute value could not be empty!");
        }
        scanPackage(scanPackagePath);
    }

    protected void scanPackage(String scanPackagePath) {
        String[] scanPackagePaths = scanPackagePath.split(",");
        ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(getBeanDefinitionRegistry());
        scanner.scan(scanPackagePaths);
    }

    protected void doParseElements(List<Element> beanElements) throws ClassNotFoundException {
        for (Element node : beanElements) {
            doParseElementAndRegistry( node);
        }
    }

    /**
     * 解析节点 bean定义
     * 包含属性
     * */
    protected void doParseElementAndRegistry(Element bean) throws ClassNotFoundException {
        String id = bean.attributeValue(XmlTagEnum.ID.tag());
        String name = bean.attributeValue(XmlTagEnum.NAME.tag());
        String className = bean.attributeValue(XmlTagEnum.CLASS_TAG.tag());
        String initMethodName = bean.attributeValue(XmlTagEnum.INIT_METHOD.tag());
        String destroyMethodName = bean.attributeValue(XmlTagEnum.DESTROY_METHOD.tag());
        String scope = bean.attributeValue(XmlTagEnum.SCOPE.tag());
        // 类对象
        Class<?> beanClazz = Class.forName(className);
        // bean名称
        String beanName = StringUtils.isNotEmpty(id) ? id : name;
        if (StringUtils.isEmpty(beanName)) {
            beanName = StrUtil.lowerFirst(beanClazz.getSimpleName());
        }
        BeanDefinition beanDefinition = new BeanDefinition(beanClazz);
        beanDefinition.setInitMethodName(initMethodName);
        beanDefinition.setDestroyMethodName(destroyMethodName);
        if (StringUtils.isNotEmpty(scope)) {
            beanDefinition.setScope(scope);
        }
        // 解析属性
        List<Element> propElements = bean.elements(XmlTagEnum.PROPERTY.tag());
        for (Element propertyEle : propElements) {
            String attrName = propertyEle.attributeValue(XmlTagEnum.NAME.tag());
            String attrValue = propertyEle.attributeValue(XmlTagEnum.VALUE.tag());
            // 对象引用
            String ref = propertyEle.attributeValue(XmlTagEnum.REF.tag());
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
