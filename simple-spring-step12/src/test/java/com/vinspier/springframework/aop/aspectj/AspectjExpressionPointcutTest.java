package com.vinspier.springframework.aop.aspectj;

import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.StockServiceImpl;
import com.vinspier.springframework.aop.AdvisedSupport;
import com.vinspier.springframework.aop.TargetSource;
import com.vinspier.springframework.aop.framework.CglibAopProxy;
import com.vinspier.springframework.aop.framework.JdkDynamicAopProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

import java.lang.reflect.Method;

public class AspectjExpressionPointcutTest {

    private StockService stockService = new StockServiceImpl();

    private AspectjExpressionPointcut pointcut = new AspectjExpressionPointcut("execution(* com.vinspier.biz.service.*.*(..))");

    private MethodInterceptor stockInterceptor = new MethodInterceptor() {
        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            System.out.println("--------> 进入方法拦截 方法:" + methodInvocation.getMethod().getName());
            return methodInvocation.proceed();
        }
    };

    private AdvisedSupport advisedSupport = new AdvisedSupport(new TargetSource(stockService),stockInterceptor,pointcut);

    @Test
    public void testAspectjMatches() throws NoSuchMethodException {
        AspectjExpressionPointcut pointcut = new AspectjExpressionPointcut("execution(* com.vinspier.biz.service.*.*(..))");
        Class<StockService> clazz = StockService.class;
        Method method = clazz.getDeclaredMethod("getWarehouse");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method,clazz));
    }

    @Test
    public void testDynamicProxy() {
        CglibAopProxy proxy = new CglibAopProxy(advisedSupport);
        StockService stockService = (StockService) proxy.getProxy();
        System.out.println(stockService.getWarehouse());
        System.out.println(stockService.getClass().getName());
        System.out.println("==================================");
        JdkDynamicAopProxy proxy1 = new JdkDynamicAopProxy(advisedSupport);
        StockService stockService1 = (StockService) proxy1.getProxy();
        System.out.println(stockService1.getWarehouse());
        System.out.println(stockService1.getClass().getName());
    }

}