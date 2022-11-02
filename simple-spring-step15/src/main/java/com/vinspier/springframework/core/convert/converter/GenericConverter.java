package com.vinspier.springframework.core.convert.converter;

import java.util.Set;

/**
 * 通用转换
 *
 * @author  xiaobiao.fan
 * @date    2022/11/1 6:23 下午
*/
public interface GenericConverter {


    Object convert(Object source,Class<?> sourceType,Class<?> targetType);

    Set<ConvertiblePair> getConvertiblePairTypes();

    /**
     * 转换类型 配对
     * */
   final class ConvertiblePair {

       private final Class<?> sourceType;

       private final Class<?> targetType;

       public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
           this.sourceType = sourceType;
           this.targetType = targetType;
       }

       public Class<?> getSourceType() {
           return sourceType;
       }

       public Class<?> getTargetType() {
           return targetType;
       }

       @Override
       public int hashCode() {
           return (sourceType.hashCode() >> 16 | targetType.hashCode()) & (sourceType.hashCode() | targetType.hashCode() >> 16);
       }

       @Override
       public boolean equals(Object obj) {
           if (null == obj) {
               return false;
           }
           if (this == obj) {
               return true;
           }
           if (!(obj instanceof ConvertiblePair)) {
               return false;
           }
           ConvertiblePair pair = (ConvertiblePair) obj;
           if (this.hashCode() != pair.hashCode()) {
               return false;
           }

           return sourceType.equals(pair.sourceType) && targetType.equals(pair.getTargetType());
       }
   }

}
