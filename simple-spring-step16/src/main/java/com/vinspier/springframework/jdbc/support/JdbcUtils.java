package com.vinspier.springframework.jdbc.support;

import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {

    public static void closeStatement(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
