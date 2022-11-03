package com.vinspier.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询结果 提取器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/3 3:39 下午
*/
public interface ResultSetExtractor<T> {

    T extractData(ResultSet resultSet) throws SQLException;

}
