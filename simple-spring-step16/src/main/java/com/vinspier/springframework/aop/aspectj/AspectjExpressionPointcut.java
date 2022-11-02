package com.vinspier.springframework.aop.aspectj;

import com.vinspier.springframework.aop.ClassFilter;
import com.vinspier.springframework.aop.MethodMatcher;
import com.vinspier.springframework.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 切面规则 判定实现
 * 委托 org.aspectj.weaver.tools 能力
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 6:03 下午
*/
public class AspectjExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private static final Set<PointcutPrimitive> SUPPORTED_POINTCUT_KINDS = new HashSet<>();

    static {
        SUPPORTED_POINTCUT_KINDS.add(PointcutPrimitive.EXECUTION);
    }

    /**
     * expression designators 表达式语法
     * https://www.baeldung.com/spring-aop-pointcut-tutorial
     * */
    private final PointcutExpression pointcutExpression;

    public AspectjExpressionPointcut(String expression) {
        PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_POINTCUT_KINDS,this.getClass().getClassLoader());
        this.pointcutExpression = parser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClazz) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
