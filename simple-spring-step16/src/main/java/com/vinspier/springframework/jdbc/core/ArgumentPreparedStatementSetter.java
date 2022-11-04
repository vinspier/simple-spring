package com.vinspier.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译执行 参数设置器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/4 5:04 下午
*/
public class ArgumentPreparedStatementSetter implements PreparedStatementSetter {

    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    /**
     * 设置参数 调用mysql原生的接口
     * */
    @Override
    public void setValues(PreparedStatement pst) throws SQLException {
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1,args[i]);
            }
        }
    }

}
