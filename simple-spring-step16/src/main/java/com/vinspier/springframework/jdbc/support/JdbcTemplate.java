package com.vinspier.springframework.jdbc.support;

import com.vinspier.springframework.jdbc.UncategorizedSQLException;
import com.vinspier.springframework.jdbc.core.JdbcOperations;
import com.vinspier.springframework.jdbc.core.SqlProvider;
import com.vinspier.springframework.jdbc.core.StatementCallback;
import com.vinspier.springframework.jdbc.datasource.DatasourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * jdbc操作模版
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:23 下午
*/
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {

    // 获取批次大小
    private int fetchSize = -1;
    // 最大行限制
    private int maxRows = -1;
    // 查询超时配置
    private int queryTimeout = -1;

    public JdbcTemplate() {

    }

    public JdbcTemplate(DataSource dataSource) {
        super.setDataSource(dataSource);
        super.afterPropertiesSet();
    }

    @Override
    public void execute(String sql) {
        class ExecuteStatementRollback implements StatementCallback<Object>, SqlProvider {
            @Override
            public String getSql() {
                return sql;
            }

            @Override
            public Object doInStatement(Statement statement) throws SQLException {
                statement.execute(sql);
                return null;
            }
        }
        // 执行
        execute(new ExecuteStatementRollback(),true);
    }

    @Override
    public <T> T execute(StatementCallback<T> callback) {
        return execute(callback,true);
    }

    private <T> T execute(StatementCallback<T> callback, boolean closeResource) {
        Connection connection = DatasourceUtils.getConnection(obtainDataSource());
        Statement statement = null;
        try {
            statement = connection.createStatement();
            // 设置stmt基础参数
            applyStatementSettings(statement);
            return callback.doInStatement(statement);
        } catch (SQLException e) {
            String sql = getSql(callback);
            JdbcUtils.closeStatement(statement);
            throw new UncategorizedSQLException("",sql,e);
        } finally {
            if (closeResource) {
                JdbcUtils.closeStatement(statement);
            }
        }
    }

    /**
     * 获取sql
     * */
    private String getSql(Object object) {
        if (object instanceof SqlProvider) {
            return ((SqlProvider) object).getSql();
        }
        return null;
    }

    protected void applyStatementSettings(Statement statement) throws SQLException {
        if (getFetchSize() > 0) {
            statement.setFetchSize(getFetchSize());
        }
        if (getMaxRows() > 0) {
            statement.setMaxRows(getMaxRows());
        }
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
}
