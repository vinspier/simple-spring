package com.vinspier.springframework.core.convert;

/**
 * 类型转换行为 逻辑抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 6:17 下午
*/
public interface ConversionService {

    /**
     * 可行性校验
     * */
    boolean canConvert(Class<?> sourceType,Class<?> targetType);

    /**
     * 转换成 T 目标结果
     * */
    <T> T convert(Object source,Class<?> targetType);

}
