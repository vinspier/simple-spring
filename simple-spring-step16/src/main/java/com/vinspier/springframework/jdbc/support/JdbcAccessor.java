package com.vinspier.springframework.jdbc.support;

import com.vinspier.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

/**
 * 数据资源访问器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/2 6:18 下午
*/
public abstract class JdbcAccessor implements InitializingBean {

    /**
     * 数据源配置
     * */
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() {
        if (null == dataSource) {
            throw new IllegalArgumentException("dataSource must can not be null!");
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource obtainDataSource() {
        return getDataSource();
    }

}
