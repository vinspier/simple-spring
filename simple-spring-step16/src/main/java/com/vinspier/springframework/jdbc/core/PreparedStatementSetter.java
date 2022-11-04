package com.vinspier.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译声明 参数填充抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/11/4 5:00 下午
*/
public interface PreparedStatementSetter {

    void setValues(PreparedStatement pst) throws SQLException;

}
