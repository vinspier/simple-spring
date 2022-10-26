package com.vinspier.springframework.aop.aspectj;

import com.vinspier.biz.aop.CustomizeBeforeAdvice;
import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.StockServiceImpl;
import com.vinspier.springframework.aop.AdvisedSupport;
import com.vinspier.springframework.aop.TargetSource;
import com.vinspier.springframework.aop.framework.ProxyFactory;
import com.vinspier.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import com.vinspier.springframework.context.support.ClasspathApplicationContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;

public class AspectjExpressionPointcutAdvisorTest {

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
    public void testBeforeAdvice() {
        CustomizeBeforeAdvice customizeBeforeAdvice = new CustomizeBeforeAdvice();
        MethodBeforeAdviceInterceptor methodBeforeAdviceInterceptor = new MethodBeforeAdviceInterceptor(customizeBeforeAdvice);
        advisedSupport.setMethodInterceptor(methodBeforeAdviceInterceptor);
        advisedSupport.setCglibProxyType(true);
        StockService stockService = (StockService) ProxyFactory.proxy(advisedSupport);
        System.out.println(stockService.getClass());
    }

    @Test
    public void testAop() {
        String configLocation = "classpath:spring.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        StockService stockService = (StockService) applicationContext.getBean("stockService");
        String warehouse = stockService.getWarehouse();
        System.out.println("方法结果：" + warehouse);
    }

}