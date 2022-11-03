package com.vinspier.springframework.jdbc.datasource;

import com.vinspier.springframework.jdbc.JdbcConnectionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatasourceUtils {

    public static Connection getConnection(DataSource dataSource) {
        Connection connection;
        try {
            connection = doGetConnection(dataSource);
        } catch (SQLException e) {
            throw new JdbcConnectionException("connection get failed",e);
        }
        return connection;
    }

    private static Connection doGetConnection(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        if (null == connection) {
            throw new SQLException("could not get connection");
        }
        return connection;
    }

}
