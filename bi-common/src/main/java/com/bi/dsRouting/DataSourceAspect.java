package com.bi.dsRouting;

/**
 * Created by bi on 2017/10/31.
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class DataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        DataSourceHolder.clearDataSourceType();
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        if (method.isAnnotationPresent(DataSource.class)) {
            DataSource datasource = method.getAnnotation(DataSource.class);
            DataSourceHolder.setDataSourceType(datasource.value());
            logger.info("==>>切换数据源切换成功<<== 数据源:{} class:{} method:{}", datasource.value(), DataSource.class.getName(), method);
        }
    }
}
