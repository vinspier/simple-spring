package com.vinspier.springframework.core.io;

public class FileSystemResourceLoader extends AbstractResourceLoader {

    @Override
    public Resource loadResource(String location) {
        Resource resource = super.loadResource(location);
        if (null == resource) {
            resource = new FileSystemResource(location);
        }
        return resource;
    }

}
