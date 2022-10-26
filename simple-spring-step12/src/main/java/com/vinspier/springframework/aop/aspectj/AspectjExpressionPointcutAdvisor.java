package com.vinspier.springframework.aop.aspectj;

import com.vinspier.springframework.aop.Pointcut;
import com.vinspier.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 切面具体实现处理
 *
 * @author  xiaobiao.fan
 * @date    2022/10/24 3:06 下午
*/
public class AspectjExpressionPointcutAdvisor implements PointcutAdvisor {

    private final String expression;

    private AspectjExpressionPointcut pointcut;

    private Advice advice;

    public AspectjExpressionPointcutAdvisor(String expression) {
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectjExpressionPointcut(expression);
        }
        return pointcut;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

}
