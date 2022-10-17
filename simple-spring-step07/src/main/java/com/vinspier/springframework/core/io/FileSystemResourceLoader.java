package com.vinspier.springframework.core.io;

public class FileSystemResourceLoader extends AbstractResourceLoader {

    @Override
    public Resource loadResource(String location) {
        return new FileSystemResource(location);
    }

}
