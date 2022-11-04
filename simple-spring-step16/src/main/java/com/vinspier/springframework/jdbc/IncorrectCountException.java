package com.vinspier.springframework.jdbc;

/**
 * 未分类错误
 */
public class IncorrectCountException extends RuntimeException{

    private final int expectedCount;

    private final int actualCount;

    public IncorrectCountException(int expectedCount, int actualCount) {
        super(String.format("incorrect count: expected count [%s] ,but there actually returned [%s]",expectedCount,actualCount));
        this.expectedCount = expectedCount;
        this.actualCount = actualCount;
    }

}
