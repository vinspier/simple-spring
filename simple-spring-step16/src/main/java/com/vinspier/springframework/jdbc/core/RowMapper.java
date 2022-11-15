package com.vinspier.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询结果 行数据映射
 *
 * @author  xiaobiao.fan
 * @date    2022/11/3 2:08 下午
*/
public interface RowMapper<T> {

    /**
     * @param resultSet 查询结果原始数据
     * @param rowIndex 数据行索引
     * @return T 返回结果类型
     * */
    T mapRow(ResultSet resultSet,int rowIndex) throws SQLException;

}
