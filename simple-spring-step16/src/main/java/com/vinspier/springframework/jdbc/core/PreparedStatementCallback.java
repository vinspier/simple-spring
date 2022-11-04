package com.vinspier.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译sql执行 声明回调
 *
 * @author  xiaobiao.fan
 * @date    2022/11/4 4:57 下午
*/
public interface PreparedStatementCallback<T> {

    T doInPreparedStatement(PreparedStatement pst) throws SQLException;

}
