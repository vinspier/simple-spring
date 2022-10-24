package com.vinspier.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * bean 配置数据源读取 行为定义
 * */
public interface Resource {

    InputStream getInputStream() throws IOException;

    /**
     * 资源来源地址
     * */
    String getResourceOrigin();

}
