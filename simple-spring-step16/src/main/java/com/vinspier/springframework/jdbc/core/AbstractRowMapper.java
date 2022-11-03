package com.vinspier.springframework.jdbc.core;

import com.vinspier.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.sql.*;

public abstract class AbstractRowMapper<T> implements RowMapper<T> {

    protected String getColumnName(ResultSetMetaData rsMetaData,int index) throws SQLException {
        String name = rsMetaData.getColumnLabel(index);
        if (null == name) {
            name = rsMetaData.getColumnName(index);
        }
        return name;
    }

    protected String getColumnKey(ResultSetMetaData rsMetaData,int index) throws SQLException {
        return getColumnName(rsMetaData,index);
    }

    protected Object getColumnValue(ResultSet rs,int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (null != obj) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        } else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        } else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        } else if (null != className && className.startsWith("oracle.sql.DATE")) {
            String metadataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metadataClassName) || "oracle.sql.TIMESTAMP".equals(metadataClassName)) {
                obj = rs.getTimestamp(index);
            } else {
                obj = rs.getDate(index);
            }
        } else if (obj instanceof Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getDate(index);
            }
        }

        return obj;
    }

    protected Object getColumnValue(ResultSet rs,int index,Class<?> requiredType) throws SQLException {
        if (null == requiredType) {
            return getColumnValue(rs, index);
        }
        Object value;
        if (String.class == requiredType) {
            return rs.getString(index);
        } else if (boolean.class == requiredType || Boolean.class == requiredType) {
            value = rs.getBoolean(index);
        } else if (byte.class == requiredType || Byte.class == requiredType) {
            value = rs.getByte(index);
        } else if (short.class == requiredType || Short.class == requiredType) {
            value = rs.getShort(index);
        } else if (int.class == requiredType || Integer.class == requiredType) {
            value = rs.getInt(index);
        } else if (long.class == requiredType || Long.class == requiredType) {
            value = rs.getLong(index);
        } else if (float.class == requiredType || Float.class == requiredType) {
            value = rs.getFloat(index);
        } else if (double.class == requiredType || Double.class == requiredType ||
                Number.class == requiredType) {
            value = rs.getDouble(index);
        } else if (BigDecimal.class == requiredType) {
            return rs.getBigDecimal(index);
        } else if (Date.class == requiredType) {
            return rs.getDate(index);
        } else if (Time.class == requiredType) {
            return rs.getTime(index);
        } else if (Timestamp.class == requiredType || java.util.Date.class == requiredType) {
            return rs.getTimestamp(index);
        } else if (byte[].class == requiredType) {
            return rs.getBytes(index);
        } else if (Blob.class == requiredType) {
            return rs.getBlob(index);
        } else if (Clob.class == requiredType) {
            return rs.getClob(index);
        } else if (requiredType.isEnum()) {
            Object obj = rs.getObject(index);
            if (obj instanceof String) {
                return obj;
            } else if (obj instanceof Number) {
                return NumberUtils.convertNumberToTargetClass(((Number) obj), Integer.class);
            } else {
                return rs.getString(index);
            }

        } else {
            return rs.getObject(index, requiredType);
        }

        return (rs.wasNull() ? null : value);
    }

}
