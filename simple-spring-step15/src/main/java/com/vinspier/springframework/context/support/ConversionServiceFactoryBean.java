package com.vinspier.springframework.context.support;

import com.vinspier.springframework.beans.factory.FactoryBean;
import com.vinspier.springframework.beans.factory.InitializingBean;
import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.core.convert.converter.Converter;
import com.vinspier.springframework.core.convert.converter.ConverterFactory;
import com.vinspier.springframework.core.convert.converter.ConverterRegistry;
import com.vinspier.springframework.core.convert.converter.GenericConverter;
import com.vinspier.springframework.core.convert.suport.DefaultConversionService;
import com.vinspier.springframework.core.convert.suport.GenericConversionService;

import java.util.Set;

/**
 * 转换器工厂bean
 * 给系统外部提供 自定义化注册入口
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 1:53 下午
*/
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    /**
     * 外部自定义 类型转换器
     * */
    private Set<?> converters;

    private GenericConversionService conversionService;

    public ConversionServiceFactoryBean() {
    }

    public ConversionServiceFactoryBean(GenericConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters,conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    @Override
    public ConversionService getObject() {
        return conversionService;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

}
