package com.vinspier.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

public class URLResourceLoader extends AbstractResourceLoader {

    @Override
    public Resource loadResource(String location) {
        try {
            return new URIResource(new URL(location));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url count not be accessed for path: " + location);
        }
    }

}
