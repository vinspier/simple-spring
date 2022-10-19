package com.vinspier.springframework.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 系统路径 文件资源
 * */
public class FileSystemResource implements Resource {

    private String path;

    private File file;

    public FileSystemResource(String path) {
        this(path,new File(path));
    }

    public FileSystemResource(String path, File file) {
        this.path = path;
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public String getResourceOrigin() {
        return getPath();
    }

    public String getPath() {
        return path;
    }
}
