package com.vinspier.springframework.aop;

/**
 * 切面通知处理 抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/10/24 3:05 下午
*/
public interface PointcutAdvisor extends Advisor {

    /**
     * 获取切面切点
     * */
    Pointcut getPointcut();

}
