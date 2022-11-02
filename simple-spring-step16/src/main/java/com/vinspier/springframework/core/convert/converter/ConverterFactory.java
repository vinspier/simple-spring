package com.vinspier.springframework.core.convert.converter;

/**
 * 转换器工厂
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 5:36 下午
*/
public interface ConverterFactory<S,R> {

    /**
     * 获取转换器
     * */
    <T extends R> Converter<S,T> getConverter(Class<T> targetClazz);

}
