package com.vinspier.springframework.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 网络数据流 资源
 * */
public class URIResource implements Resource {

    private URL url;

    public URIResource(URL url) {
        if (null == url) {
            throw new IllegalArgumentException("url must not be null！");
        }
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection connection = this.url.openConnection();
        try {
            InputStream inputStream = connection.getInputStream();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
            if (connection instanceof HttpURLConnection) {
                // 关闭链接
                ((HttpURLConnection) connection).disconnect();
            }
            throw e;
        }
    }

    @Override
    public String getResourceOrigin() {
        return this.url.getPath();
    }
}
