package com.vinspier.springframework.jdbc.core;

import java.util.List;
import java.util.Map;

/**
 * jdbc操作抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:17 下午
*/
public interface JdbcOperations {

    //==================== basic jdbc execute operation support ====================
    /**
     * 简单直接执行sql
     * */
    void execute(String sql);

    /**
     * 执行外部自定义callback
     * */
    <T> T execute(StatementCallback<T> callback);
    //==================== basic jdbc execute operation support ====================

    //==================== basic query operation support ====================
    <T> List<T> query(String sql, RowMapper<T> rowMapper);

    <T> T query(String sql,ResultSetExtractor<T> extractor);
    //==================== basic query operation support ====================

    //==================== list query operation support ====================
    /**
     * 查询 行列表
     * */
    List<Map<String,Object>> queryForList(String sql);
    //==================== list query operation support ====================
}
