package com.vinspier.springframework.core.convert.converter;

/**
 * 转换器注册 抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 7:39 下午
*/
public interface ConverterRegistry {

    void addConverter(Converter<?,?> converter);

    void addConverter(GenericConverter converter);

    void addConverterFactory(ConverterFactory<?,?> factory);

}
