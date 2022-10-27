package com.vinspier.springframework.util;

public class StringUtils {

    public static boolean isEmpty(String s) {
        return null == s || s.length() < 1;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

}
