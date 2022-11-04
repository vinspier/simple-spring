package com.vinspier.springframework.jdbc.core;

import com.vinspier.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class AbstractRowMapper<T> implements RowMapper<T> {

    protected String getColumnName(ResultSetMetaData rsMetaData,int index) throws SQLException {
        return JdbcUtils.getColumnName(rsMetaData,index);
    }

    protected String getColumnKey(ResultSetMetaData rsMetaData,int index) throws SQLException {
        return getColumnName(rsMetaData,index);
    }

    protected Object getColumnValue(ResultSet rs,int index) throws SQLException {
        return JdbcUtils.getColumnValue(rs,index);
    }

    protected Object getColumnValue(ResultSet rs,int index,Class<?> requiredType) throws SQLException {
        return JdbcUtils.getColumnValue(rs,index,requiredType);
    }

}
