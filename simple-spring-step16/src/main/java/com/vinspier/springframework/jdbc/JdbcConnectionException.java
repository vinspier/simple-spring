package com.vinspier.springframework.jdbc;

import java.sql.SQLException;

/**
 * 链接异常
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:39 下午
*/
public class JdbcConnectionException extends RuntimeException {

    public JdbcConnectionException(String message) {
        super(message);
    }

    public JdbcConnectionException(String message, SQLException ex) {
        super(message, ex);
    }
}
