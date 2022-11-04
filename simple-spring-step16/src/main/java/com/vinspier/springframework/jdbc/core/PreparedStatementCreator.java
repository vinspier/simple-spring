package com.vinspier.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译声明 生成器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/4 5:00 下午
*/
public interface PreparedStatementCreator {

    PreparedStatement getPreparedStatement(Connection connection) throws SQLException;

}
