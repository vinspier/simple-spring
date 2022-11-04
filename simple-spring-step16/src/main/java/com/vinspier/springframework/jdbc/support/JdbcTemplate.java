package com.vinspier.springframework.jdbc.support;

import com.vinspier.springframework.jdbc.IncorrectCountException;
import com.vinspier.springframework.jdbc.UncategorizedSQLException;
import com.vinspier.springframework.jdbc.core.*;
import com.vinspier.springframework.jdbc.datasource.DatasourceUtils;
import com.vinspier.springframework.util.CollectionsUtils;
import com.vinspier.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
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
            throw new UncategorizedSQLException("statement execute failed ",sql,e);
        } finally {
            if (closeResource) {
                JdbcUtils.closeStatement(statement);
            }
        }
    }

    @Override
    public <T> T execute(String sql, ResultSetExtractor<T> extractor,Object... args) {
        PreparedStatementCreator statementCreator = new SimplePreparedStatementCreator(sql);
        PreparedStatementSetter statementSetter = getArgsPreparedStatementSetter(args);
        return execute(statementCreator,statementSetter,extractor);
    }

    @Override
    public <T> T execute(PreparedStatementCreator statementCreator, PreparedStatementSetter statementSetter, ResultSetExtractor<T> extractor) {
        PreparedStatementCallback<T> callback = (pst) -> {
            ResultSet resultSet;
            if (statementSetter != null) {
                statementSetter.setValues(pst);
            }
            resultSet = pst.executeQuery();
            return extractor.extractData(resultSet);
        };
        return execute(statementCreator,callback,true);
    }

    /**
     * 底层执行预编译语句
     * */
    private  <T> T execute(PreparedStatementCreator statementCreator,PreparedStatementCallback<T> callback,boolean closeResource) {
        Connection connection = DatasourceUtils.getConnection(obtainDataSource());
        PreparedStatement pst = null;
        try {
            pst = statementCreator.getPreparedStatement(connection);
            applyStatementSettings(pst);
            T result = callback.doInPreparedStatement(pst);
            return result;
        } catch (SQLException e) {
            String sql = getSql(callback);
            JdbcUtils.closeStatement(pst);
            throw new UncategorizedSQLException("prepared statement execute failed ",sql,e);
        } finally {
            if (closeResource) {
                JdbcUtils.closeStatement(pst);
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
    public <T> List<T> query(String sql, RowMapper<T> rowMapper,Object... args) {
        return query(sql,new RowMapperResultSetExtractor<>(rowMapper),args);
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> extractor,Object... args) {
        return query(sql,getArgsPreparedStatementSetter(args),extractor);
    }

    @Override
    public <T> T query(String sql, PreparedStatementSetter statementSetter, ResultSetExtractor<T> extractor) {
        PreparedStatementCreator statementCreator = new SimplePreparedStatementCreator(sql);
        return execute(statementCreator,statementSetter,extractor);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) {
        return query(sql,getColumnRowMapper());
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object[] args) {
        return query(sql,getColumnRowMapper(),args);
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> requiredType) {
        return query(sql,getSingleColumnRowMapper(requiredType));
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> requiredType,Object... args) {
        return query(sql,getSingleColumnRowMapper(requiredType),args);
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
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper,Object... args) {
        List<T> result = query(sql,new RowMapperResultSetExtractor<>(rowMapper,1),args);
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
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
        return queryForObject(sql,getSingleColumnRowMapper(requiredType),args);
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        return result(queryForObject(sql,getColumnRowMapper()));
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) {
        return result(queryForObject(sql,getColumnRowMapper(),args));
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

    protected ArgumentPreparedStatementSetter getArgsPreparedStatementSetter(Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    /**
     * 简易 预编译执行器 生成器
     * // todo 可根据Connection的prepareStatement接口能力 指定不同的 预编译执行器
     * */
    private static class SimplePreparedStatementCreator implements PreparedStatementCreator,SqlProvider {

        private final String sql;

        public SimplePreparedStatementCreator(String sql) {
            this.sql = sql;
        }

        @Override
        public String getSql() {
            return sql;
        }

        @Override
        public PreparedStatement getPreparedStatement(Connection connection) throws SQLException {
            return connection.prepareStatement(sql);
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
