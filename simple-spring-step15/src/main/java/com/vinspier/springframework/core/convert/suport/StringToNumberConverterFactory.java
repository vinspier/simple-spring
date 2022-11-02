package com.vinspier.springframework.core.convert.suport;

import com.vinspier.springframework.core.convert.converter.Converter;
import com.vinspier.springframework.core.convert.converter.ConverterFactory;
import com.vinspier.springframework.util.NumberUtils;

/**
 * 字符串2数字 转换器工厂
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 5:36 下午
*/
public class StringToNumberConverterFactory implements ConverterFactory<String,Number> {

    @Override
    public <T extends Number> Converter<String,T> getConverter(Class<T> targetType) {
        return new StringToNumberConverter<>(targetType);
    }

    public static class StringToNumberConverter<T extends Number> implements Converter<String,T> {

        private final Class<T> targetType;

        public StringToNumberConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String string) {
            return NumberUtils.parseNumber(string,targetType);
        }

    }

}
