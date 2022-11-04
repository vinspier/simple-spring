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

    /**
     * 预编译执行
     * */
    <T> T execute(String sql,ResultSetExtractor<T> extractor,Object... args);

    <T> T execute(PreparedStatementCreator statementCreator,PreparedStatementSetter statementSetter,ResultSetExtractor<T> extractor);

    //==================== basic jdbc execute operation support ====================

    //==================== basic query operation support ====================
    <T> List<T> query(String sql, RowMapper<T> rowMapper);

    <T> T query(String sql,ResultSetExtractor<T> extractor);

    <T> List<T> query(String sql, RowMapper<T> rowMapper,Object... args);

    <T> T query(String sql,ResultSetExtractor<T> extractor,Object... args);

    <T> T query(String sql,PreparedStatementSetter statementSetter,ResultSetExtractor<T> extractor);
    //==================== basic query operation support ====================

    //==================== list query operation support ====================
    /**
     * 列表查询 指定行转换器
     * */
    List<Map<String,Object>> queryForList(String sql);

    /**
     * 预编译 列表查询
     * sql条件占位符 args可传参数
     * */
    List<Map<String,Object>> queryForList(String sql,Object... args);

    /**
     * 列表 单字段查询
     * */
    <T> List<T> queryForList(String sql,Class<T> requiredType);

    /**
     * 预编译 列表查询 单字段查询
     * sql条件占位符 args可传参数
     * */
    <T> List<T> queryForList(String sql,Class<T> requiredType,Object... args);
    //==================== list query operation support ====================

    //==================== single object query operation support ====================
    /**
     * 查询 指定行转换器
     *
     * */
    <T> T queryForObject(String sql,RowMapper<T> rowMapper);

    /**
     * 预编译查询 指定行转换器
     * sql条件占位符 args可传参数
     * */
    <T> T queryForObject(String sql,RowMapper<T> rowMapper,Object... args);

    /**
     * 单行单列字段查询
     * */
    <T> T queryForObject(String sql,Class<T> requiredType);

    /**
     * 预编译 单行单列字段查询
     * */
    <T> T queryForObject(String sql,Class<T> requiredType,Object... args);
    //==================== single object query operation support ====================

    //==================== map query operation support ====================
    /**
     * 查询 单行记录 解析列数据
     * */
    Map<String,Object> queryForMap(String sql);

    /**
     * 预编译查询 单行记录 解析列数据
     * sql条件占位符 args可传参数
     * */
    Map<String,Object> queryForMap(String sql,Object... args);

    //==================== map query operation support ====================

}
