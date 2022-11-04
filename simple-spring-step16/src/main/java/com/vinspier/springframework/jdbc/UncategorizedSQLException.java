package com.vinspier.springframework.jdbc;

/**
 * 未分类错误
 */
public class UncategorizedSQLException extends RuntimeException{


    public UncategorizedSQLException(String message) {
        super(message);
    }

    public UncategorizedSQLException(String desc, String sql, Throwable cause) {
        super(String.format("%s sql: %s",desc,sql), cause);
    }
}
