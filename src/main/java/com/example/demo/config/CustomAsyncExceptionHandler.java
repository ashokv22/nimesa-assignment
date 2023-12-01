package com.example.demo.config;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... obj) {
        log.error("Unexpected exception occurred invoking async method: " + method, ex);
        if (log.isDebugEnabled()) {
            for (Object param : obj) {
                log.error("Parameter value - " + param);
            }
        }
    }
}
