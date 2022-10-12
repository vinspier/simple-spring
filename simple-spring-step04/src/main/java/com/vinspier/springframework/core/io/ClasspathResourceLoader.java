package com.vinspier.springframework.core.io;

public class ClasspathResourceLoader  extends  AbstractResourceLoader {

    @Override
    public Resource loadResource(String location) {
        Resource resource = super.loadResource(location);
        if (null == resource) {
            return new ClasspathResource(location.substring(CLASSPATH_PREFIX.length()));
        }
        return resource;
    }

}
