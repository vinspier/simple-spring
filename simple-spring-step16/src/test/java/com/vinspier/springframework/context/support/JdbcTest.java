package com.vinspier.springframework.context.support;

import com.vinspier.springframework.jdbc.core.JdbcOperations;
import com.vinspier.springframework.jdbc.support.JdbcTemplate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JdbcTest {

    private JdbcOperations jdbcTemplate;

    @Before
    public void init() {
        String configLocation = "classpath:spring-jdbc-annotation.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        this.jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
    }

    @Test
    public void testExecuteSql() {
        String sql = "SELECT COUNT(1) FROM tb_spu";
        jdbcTemplate.execute(sql);
    }

    @Test
    public void testQueryForList() {
        String sql = "SELECT * FROM tb_spu limit 10";
        List<Map<String,Object>> resultMap = jdbcTemplate.queryForList(sql);
        System.out.println(resultMap.size());
    }

}