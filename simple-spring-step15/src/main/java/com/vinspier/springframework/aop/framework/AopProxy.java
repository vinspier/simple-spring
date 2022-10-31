package com.vinspier.springframework.aop.framework;

/**
 * 面向切面代理 抽象定义
 *
 * @author  xiaobiao.fan
 * @date    2022/10/21 11:02 上午
*/
public interface AopProxy {

    /**
     * 获取代理对象
     * */
    Object getProxy();

}
