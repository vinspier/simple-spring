package com.vinspier.springframework.jdbc;

/**
 * 未分类错误
 */
public class IncorrectResultSetColumnCountException extends IncorrectCountException {

    public IncorrectResultSetColumnCountException(int expectedCount, int actualCount) {
        super(expectedCount,actualCount);
    }

}
