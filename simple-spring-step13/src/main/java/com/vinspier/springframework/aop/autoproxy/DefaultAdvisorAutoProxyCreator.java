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

    /**
     * 动态代理对象创建 在bean对象填充完所有属性最后执行
     * */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        if (isInfrastructureClass(clazz)) {
            return bean;
        }
        Collection<AspectjExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectjExpressionPointcutAdvisor.class).values();
        for (AspectjExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(clazz)) {
                continue;
            }
            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setCglibProxyType(false);
            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource);
            // 代理工厂
            return ProxyFactory.proxy(advisedSupport);
        }
        return bean;
    }

    /**
     * 为符合进入切面的类 生成代理对象
     * */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
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
