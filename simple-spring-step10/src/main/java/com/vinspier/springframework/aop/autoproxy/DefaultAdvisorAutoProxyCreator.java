package com.vinspier.springframework.aop.autoproxy;

import com.vinspier.springframework.aop.*;
import com.vinspier.springframework.aop.aspectj.AspectjExpressionPointcutAdvisor;
import com.vinspier.springframework.aop.framework.ProxyFactory;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.beans.factory.BeanFactoryAware;
import com.vinspier.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.vinspier.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * 符合切面的对象 代理工厂会代理出一个对象
 *
 * @author  xiaobiao.fan
 * @date    w 4:23 下午
*/
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 为符合进入切面的类 生成代理对象
     * */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        if (isInfrastructureClass(beanClass)) {
            return null;
        }
        Collection<AspectjExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectjExpressionPointcutAdvisor.class).values();
        for (AspectjExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }
            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setCglibProxyType(true);
            try {
                TargetSource targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
                advisedSupport.setTargetSource(targetSource);
                // 代理工厂
                return ProxyFactory.proxy(advisedSupport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isInfrastructureClass(Class<?> clazz) {
        return Advice.class.isAssignableFrom(clazz) || Pointcut.class.isAssignableFrom(clazz) || Advisor.class.isAssignableFrom(clazz);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
