package com.vinspier.springframework.util;

/**
 * 配置文件属性配置值字符串处理器
 *
 * @author  xiaobiao.fan
 * @date    2022/10/26 2:07 下午
*/
public interface StringValueResolver {

    /**
     * 处理属性配置值表达式 从配置文件获取值
     * @param valueExpression value表达式
     */
    String resolve(String valueExpression);

}
