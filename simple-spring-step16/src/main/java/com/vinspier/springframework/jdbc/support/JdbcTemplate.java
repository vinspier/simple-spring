package com.vinspier.springframework.jdbc.support;

import com.vinspier.springframework.jdbc.IncorrectCountException;
import com.vinspier.springframework.jdbc.UncategorizedSQLException;
import com.vinspier.springframework.jdbc.core.*;
import com.vinspier.springframework.jdbc.datasource.DatasourceUtils;
import com.vinspier.springframework.util.CollectionsUtils;
import com.vinspier.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

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
        setDataSource(dataSource);
        afterPropertiesSet();
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

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return result(query(sql,new RowMapperResultSetExtractor<T>(rowMapper)));
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> extractor) {
        if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException("query sql statement must not be empty!");
        }
        if (null == extractor) {
            throw new IllegalArgumentException("result set extractor must not be null!");
        }
        class QueryStatementCallback implements StatementCallback<T>,SqlProvider {
            @Override
            public String getSql() {
                return sql;
            }

            @Override
            public T doInStatement(Statement statement) throws SQLException {
                ResultSet rs = statement.executeQuery(sql);
                return extractor.extractData(rs);
            }
        }
        return execute(new QueryStatementCallback(),true);
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        return query(sql,args,new RowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> extractor) {
        // todo
        return null;
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) {
        return query(sql,getColumnRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object[] args) {
        return query(sql,args,getColumnRowMapper());
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> requiredType) {
        return query(sql,getSingleColumnRowMapper(requiredType));
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> requiredType) {
        return query(sql,args,getSingleColumnRowMapper(requiredType));
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        List<T> result = query(sql,rowMapper);
        if (CollectionsUtils.isEmpty(result)) {
            throw new IncorrectCountException(1,0);
        }
        if (result.size() != 1) {
            throw new IncorrectCountException(1,result.size());
        }
        return result.iterator().next();
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        List<T> result = query(sql,args,new RowMapperResultSetExtractor<>(rowMapper,1));
        if (CollectionsUtils.isEmpty(result)) {
            throw new IncorrectCountException(1,0);
        }
        if (result.size() != 1) {
            throw new IncorrectCountException(1,result.size());
        }
        return result.iterator().next();
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return queryForObject(sql,getSingleColumnRowMapper(requiredType));
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        return result(queryForObject(sql,getColumnRowMapper()));
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object[] args) {
        return result(queryForObject(sql,args,getColumnRowMapper()));
    }

    /**
     * 数据结果的处理 没有查询到数据时 应当返回一个空数据
     * 空数组、空列表、空map、空set......
     */
    protected <T> T result(T result) {
        if (null == result) {
            throw new IllegalStateException("illegal execute result state: there is no result returned!");
        }
        return result;
    }

    protected RowMapper<Map<String,Object>> getColumnRowMapper() {
        return new ColumnRowMapper();
    }

    protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
        return new SingleColumnRowMapper<>(requiredType);
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
