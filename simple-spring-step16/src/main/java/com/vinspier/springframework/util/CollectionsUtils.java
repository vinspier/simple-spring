package com.vinspier.springframework.util;

import java.util.Collection;

public class CollectionsUtils {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return null == collection || collection.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return null != collection && !collection.isEmpty();
    }

}
