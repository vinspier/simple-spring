package com.vinspier.springframework.util;

import cn.hutool.core.lang.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字转换工具类
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 6:00 下午
*/
public class NumberUtils {

    /**
     * 简单转换
     * todo: text的格式校验、数值的范围校验
     * */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {

        Assert.notNull(text, "source must not be null");
        Assert.notNull(targetClass, "target class must not be null");
        String trimmed = trimAllWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) Byte.valueOf(trimmed);
        }
        else if (Short.class == targetClass) {
            return (T) Short.valueOf(trimmed);
        }
        else if (Integer.class == targetClass) {
            return (T) Integer.valueOf(trimmed);
        }
        else if (Long.class == targetClass) {
            return (T) Long.valueOf(trimmed);
        }
        else if (BigInteger.class == targetClass) {
            return (T) new BigInteger(trimmed);
        }
        else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        }
        else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        }
        else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        }
        else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    public static String trimAllWhitespace(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
