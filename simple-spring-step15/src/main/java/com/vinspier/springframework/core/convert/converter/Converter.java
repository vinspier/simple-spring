package com.vinspier.springframework.core.convert.converter;

/**
 * 定义转换 行为
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 5:36 下午
*/
public interface Converter<S,T> {

    /**
     * 类型S 转换成 类型T
     * */
    T convert(S s);

}
