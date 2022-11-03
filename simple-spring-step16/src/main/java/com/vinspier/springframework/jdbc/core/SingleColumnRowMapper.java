package com.vinspier.springframework.jdbc.core;

import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.jdbc.IncorrectResultSetColumnCountException;
import com.vinspier.springframework.jdbc.UncategorizedSQLException;
import com.vinspier.springframework.util.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 单列数据结果集映射
 *
 * @author  xiaobiao.fan
 * @date    2022/11/3 2:13 下午
*/
public class SingleColumnRowMapper<T> extends AbstractRowMapper<T> {

    private Class<T> requiredType;

    private ConversionService conversionService;

    public SingleColumnRowMapper() {

    }

    public SingleColumnRowMapper(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    @Override
    public T mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
        ResultSetMetaData rsMetaData =  resultSet.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        if (columnCount != 1) {
            throw new IncorrectResultSetColumnCountException(1,columnCount);
        }
        Object value = getColumnValue(resultSet,1,requiredType);
        if (null != value && null != requiredType && !requiredType.isInstance(value)) {
            value = convertValueToRequiredType(value,requiredType);
        }
        return (T) value;
    }

    /**
     * 转换成预期类型值
     * todo: 可以调用上一节 的 属性转换服务ConversionService
     * */
    protected Object convertValueToRequiredType(Object value,Class<?> requiredType) {
        if (String.class == requiredType) {
            return value.toString();
        } else if (Number.class.isAssignableFrom(requiredType)) {
            // 数字类型处理
            if (value instanceof Number) {
                return NumberUtils.convertNumberToTargetClass((Number) value,(Class<Number>) requiredType);
            } else {
                return NumberUtils.parseNumber(value.toString(),(Class<Number>) requiredType);
            }
        } else if (getConversionService() != null) {
            // todo 使用spring内部提供的转换器
            return value;
        } else {
            throw new UncategorizedSQLException(String.format("can not convert value to requiredType: value = [%s]",value));
        }
    }

    public Class<T> getRequiredType() {
        return requiredType;
    }

    public void setRequiredType(Class<T> requiredType) {
        this.requiredType = requiredType;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
