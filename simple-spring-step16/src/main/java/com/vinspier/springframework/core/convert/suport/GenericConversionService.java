package com.vinspier.springframework.core.convert.suport;

import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.core.convert.converter.Converter;
import com.vinspier.springframework.core.convert.converter.ConverterFactory;
import com.vinspier.springframework.core.convert.converter.ConverterRegistry;
import com.vinspier.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 通用转换器处理中心
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 10:42 上午
*/
public class GenericConversionService implements ConversionService, ConverterRegistry {

    private final Map<GenericConverter.ConvertiblePair,GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return null != getConverter(sourceType,targetType);
    }

    @Override
    public <T> T convert(Object source, Class<?> targetType) {
        GenericConverter converter = getConverter(source.getClass(),targetType);
        return (T) converter.convert(source,source.getClass(),targetType);
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair convertiblePair = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(convertiblePair,converter);
        for (GenericConverter.ConvertiblePair pair : converterAdapter.getConvertiblePairTypes()) {
            converters.put(pair,converterAdapter);
        }
    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (GenericConverter.ConvertiblePair pair : converter.getConvertiblePairTypes()) {
            converters.put(pair,converter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        GenericConverter.ConvertiblePair convertiblePair = getRequiredTypeInfo(factory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(convertiblePair,factory);
        for (GenericConverter.ConvertiblePair pair : converterFactoryAdapter.getConvertiblePairTypes()) {
            converters.put(pair,converterFactoryAdapter);
        }
    }

    /**
     * 获取转换 类型限制
     * */
    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object converter) {
        Type[] types = converter.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type[] actualTypes = parameterizedType.getActualTypeArguments();
        Class<?> sourceType = (Class<?>) actualTypes[0];
        Class<?> targetType = (Class<?>) actualTypes[1];
        return new GenericConverter.ConvertiblePair(sourceType,targetType);
    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceCandidates = getHierarchyClasses(sourceType);
        List<Class<?>> targetCandidates = getHierarchyClasses(targetType);
        for (Class<?> s : sourceCandidates) {
            for (Class<?> t : targetCandidates) {
                GenericConverter.ConvertiblePair pair = new GenericConverter.ConvertiblePair(s,t);
                GenericConverter converter = converters.get(pair);
                if (null != converter) {
                    return converter;
                }
            }
        }
        return null;
    }

    /**
     * 逐级获取类型
     * */
    private List<Class<?>> getHierarchyClasses(Class<?> clazz) {
        List<Class<?>> hierarchy = new LinkedList<>();
        while (null != clazz) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    /**
     * 转换器适配
     * */
    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object,Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converter.convert(source);
        }

        @Override
        public Set<ConvertiblePair> getConvertiblePairTypes() {
            return Collections.singleton(typeInfo);
        }

    }

    /**
     * 转换器工厂适配
     * */
    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object,Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object,Object>) converterFactory;
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }

        @Override
        public Set<ConvertiblePair> getConvertiblePairTypes() {
            return Collections.singleton(typeInfo);
        }

    }

}
