package com.vinspier.springframework.jdbc.core;

/**
 * jdbc操作抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:17 下午
*/
public interface JdbcOperations {

    //==================== basic operation support ====================
    /**
     * 简单直接执行sql
     * */
    void execute(String sql);

    /**
     * 执行外部自定义callback
     * */
    <T> T execute(StatementCallback<T> callback);
    //==================== basic operation support ====================
    
}
