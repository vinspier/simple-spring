package com.vinspier.springframework.core.convert.suport;

import com.vinspier.springframework.core.convert.converter.ConverterRegistry;

/**
 * 转换器通过处理中心 默认内置转换器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 10:42 上午
*/
public class DefaultConversionService extends GenericConversionService {

    public DefaultConversionService() {
        addDefaultConverts(this);
    }

    /**
     * spring框架 内部默认转换器
     *
     * */
    public static void addDefaultConverts(ConverterRegistry registry) {
        // 此处 注册转换器、转换器工厂
        registry.addConverterFactory(new StringToNumberConverterFactory());
        // 其他 内部转换器
    }

}
