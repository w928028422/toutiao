package com.ayzl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.ayzl.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object obj : joinPoint.getArgs()){
            sb.append("arg:");
            sb.append(obj.toString());
            sb.append("|");
        }
        logger.info("before " + sb.toString());
    }

    @After("execution(* com.ayzl.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for(Object obj : joinPoint.getArgs()){
            sb.append("arg:");
            sb.append(obj.toString());
            sb.append("|");
        }
        logger.info("after " + sb.toString());
    }
}
