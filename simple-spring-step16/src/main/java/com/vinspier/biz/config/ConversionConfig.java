package com.vinspier.biz.config;

import com.vinspier.biz.convert.StringToLocalDateConverter;
import com.vinspier.springframework.beans.factory.FactoryBean;
import com.vinspier.springframework.context.support.ConversionServiceFactoryBean;
import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.core.convert.suport.DefaultConversionService;
import com.vinspier.springframework.core.convert.suport.GenericConversionService;
import com.vinspier.springframework.stereotype.Component;

/**
 * 属性 转换器 配置
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 11:39 上午
*/
@Component("conversionService")
public class ConversionConfig implements FactoryBean<ConversionService> {


    /**
     * 使用spring内部提供的转换器生成工厂
     * 制定 自定义转换器处理中心
     * */
    @Override
    public ConversionService getObject() {
        GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToLocalDateConverter("yyyy-MM-dd"));
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean(conversionService);
        return factoryBean.getObject();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return ConversionService.class;
    }

}
