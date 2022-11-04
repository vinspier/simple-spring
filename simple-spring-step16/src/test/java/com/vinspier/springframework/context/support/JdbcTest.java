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

    // ---------------------------- test simple statement ----------------------------
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

    @Test
    public void testQuerySingleObjectList() {
        String sql = "SELECT title FROM tb_spu limit 10";
        List<String> result = jdbcTemplate.queryForList(sql,String.class);
        System.out.println(result);
    }

    @Test
    public void testQueryForMap() {
        String sql = "SELECT * FROM tb_spu where id = 2";
        Map<String,Object> result = jdbcTemplate.queryForMap(sql);
        System.out.println(result.toString());
    }

    @Test
    public void testQuerySingleObject() {
        String sql = "SELECT title FROM tb_spu where id = 2";
        String result = jdbcTemplate.queryForObject(sql,String.class);
        System.out.println(result);
    }

    // ---------------------------- test prepared statement ----------------------------
    @Test
    public void testPreparedQueryForMap() {
        String sql = "SELECT title FROM tb_spu where id = ?";
        Map<String,Object> result = jdbcTemplate.queryForMap(sql,2);
        System.out.println(result);
    }

    @Test
    public void testPreparedQueryForList() {
        String sql = "SELECT title FROM tb_spu where id < ?";
        List<Map<String,Object>> result = jdbcTemplate.queryForList(sql,10);
        System.out.println(result);
    }

    @Test
    public void testPreparedQueryForObject() {
        String sql = "SELECT title FROM tb_spu where id = ?";
        List<Map<String,Object>> result = jdbcTemplate.queryForList(sql,10);
        System.out.println(result);
    }

}