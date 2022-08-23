package com.ibm.simulatte.core.configs.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler
        implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        System.out.println("Exception message - " + throwable.getMessage());
        System.out.println("Exception cause - " + throwable.getCause());
        System.out.println("Exception location - " + throwable.getLocalizedMessage());
        System.out.println("Method name - " + method.getName());
        System.out.println("Method exception types - " + method.getExceptionTypes());
        for (Object param : obj) {
            System.out.println("Parameter value - " + param);
        }
    }

}
