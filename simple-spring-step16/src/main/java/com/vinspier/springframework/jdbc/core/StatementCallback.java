package com.vinspier.springframework.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * sql执行申明回调
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:33 下午
*/
public interface StatementCallback<T> {

    T doInStatement(Statement statement) throws SQLException;

}
