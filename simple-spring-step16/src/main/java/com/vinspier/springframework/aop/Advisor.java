package com.vinspier.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 通知处理抽象
 *
 * @author  xiaobiao.fan
 * @date    2022/10/24 3:03 下午
*/
public interface Advisor {

    /**
     * 获取切面通知 方法
     * */
    Advice getAdvice();

}
