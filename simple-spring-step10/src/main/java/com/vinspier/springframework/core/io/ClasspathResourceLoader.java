package com.vinspier.springframework.core.io;

public class ClasspathResourceLoader extends AbstractResourceLoader {

    @Override
    public Resource loadResource(String location) {
        return new ClasspathResource(location.substring(CLASSPATH_PREFIX.length()));
    }

}
