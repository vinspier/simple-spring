package com.vinspier.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 行转列转换结果 结果提取器
 *
 * @author  xiaobiao.fan
 * @date    2022/11/3 3:42 下午
*/
public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;

    private final int expectRows;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this(rowMapper,0);
    }

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int expectRows) {
        this.rowMapper = rowMapper;
        this.expectRows = expectRows;
    }

    /**
     * 所有行数据 按列 组装成rowMapper规则转换后的数据集列表
     * */
    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException {
        List<T> resultList = createResult(expectRows);
        int rowIndex = 0;
        while (resultSet.next()) {
            resultList.add(rowMapper.mapRow(resultSet,rowIndex));
        }
        return resultList;
    }

    private List<T> createResult(int expectRows) {
        return expectRows > 0 ? new ArrayList<>(expectRows) : new LinkedList<>();
    }

}
