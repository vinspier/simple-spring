package com.vinspier.springframework.jdbc.core;

import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.jdbc.IncorrectResultSetColumnCountException;
import com.vinspier.springframework.jdbc.UncategorizedSQLException;
import com.vinspier.springframework.util.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 每行列数据 转 映射结果集
 * [{columnName:columnValue}]
 *
 * @author  xiaobiao.fan
 * @date    2022/11/3 2:13 下午
*/
public class ColumnRowMapper extends AbstractRowMapper<Map<String,Object>> {

    private ConversionService conversionService;

    public ColumnRowMapper() {

    }

    public ColumnRowMapper(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * [{columnName:columnValue}]
     * */
    @Override
    public Map<String,Object> mapRow(ResultSet resultSet, int rowIndex) throws SQLException {
        ResultSetMetaData rsMetaData =  resultSet.getMetaData();
        int columnCount = rsMetaData.getColumnCount();
        Map<String,Object> resultMap = createColumnsMap(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            String name = getColumnName(rsMetaData,i);
            Object value = getColumnValue(resultSet,i);
            resultMap.putIfAbsent(name,value);
        }
        return resultMap;
    }

    protected Map<String,Object> createColumnsMap(int columnCount) {
        return new LinkedHashMap<>(columnCount);
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
