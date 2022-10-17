package com.vinspier.springframework.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 项目路径 文件资源
 * */
public class ClasspathResource implements Resource {

    private final String path;

    private ClassLoader classLoader;

    public ClasspathResource(String path) {
        this(path,null);
    }

    public ClasspathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = classLoader.getResourceAsStream(path);
        if (null == inputStream) {
            throw new FileNotFoundException("file could not be opened as it does not existed from path: " + path);
        }
        return inputStream;
    }

    @Override
    public String getResourceOrigin() {
        return path;
    }

}
