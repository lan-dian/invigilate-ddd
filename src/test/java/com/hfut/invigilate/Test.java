package com.hfut.invigilate;

import com.sun.xml.internal.ws.server.AbstractWebServiceContext;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.BeanValidationPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.awt.*;



public class Test {

    public static void main(String[] args) {
        AutowireCapableBeanFactory autowireCapableBeanFactory;
        ApplicationContext applicationContext;
        BeanDefinition beanDefinition;
        WebApplicationContext webApplicationContext;
        BeanDefinitionRegistry beanDefinitionRegistry;
        InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor;
        BeanPostProcessor beanPostProcessor;
        ServletContext servletContext;
        AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext();
        AbstractRefreshableConfigApplicationContext abstractRefreshableConfigApplicationContext;
        AbstractApplicationContext abstractApplicationContext;
        ImportSelector importSelector;
        ImportBeanDefinitionRegistrar importBeanDefinitionRegistrar;
        AbstractAutowireCapableBeanFactory abstractAutowireCapableBeanFactory;
        ApplicationContextAware applicationContextAware;
        BeanValidationPostProcessor beanValidationPostProcessor;
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor;
        Environment environment;
        BeanFactoryPostProcessor beanFactoryPostProcessor;
        ApplicationListener<ApplicationEvent> applicationListener;
        DefaultListableBeanFactory defaultListableBeanFactory;
        //aop
        AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator;
        AbstractAdvisorAutoProxyCreator abstractAdvisorAutoProxyCreator;
        MethodBeforeAdviceInterceptor methodBeforeAdviceInterceptor;
        AspectJAfterAdvice aspectJAfterAdvice;
        AfterReturningAdviceInterceptor afterReturningAdviceInterceptor;
        AspectJAfterThrowingAdvice aspectJAfterThrowingAdvice;
    }


}
