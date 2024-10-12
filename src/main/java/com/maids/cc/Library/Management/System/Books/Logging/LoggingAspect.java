package com.maids.cc.Library.Management.System.Books.Logging;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.maids.cc.Library.Management.System.Books.*.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info("Calling method: {} with arguments: {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
    }

    @After("execution(* com.maids.cc.Library.Management.System.Books.*.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        logger.info("Method exited: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* com.maids.cc.Library.Management.System.Books.*.*(..))", throwing = "exception")
    public void logMethodException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {} with message: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }

    @Around("execution(* com.maids.cc.Library.Management.System.Books.BookController.add*(..)) || execution(* com.maids.cc.Library.Management.System.Books.BookController.update*(..))")
    public Object logPerformanceMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // Proceed with the method call
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info("Execution time of method {}: {} ms", joinPoint.getSignature().getName(), elapsedTime);
        return result;
    }
}
