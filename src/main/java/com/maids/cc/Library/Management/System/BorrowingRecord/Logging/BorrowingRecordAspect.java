package com.maids.cc.Library.Management.System.BorrowingRecord.Logging;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BorrowingRecordAspect {

    // Pointcut for all methods in BorrowingRecordController
    @Pointcut("execution(* com.maids.cc.Library.Management.System.BorrowingRecord.BorrowingRecordController.*(..))")
    public void borrowingRecordControllerMethods() {}

    @Before("borrowingRecordControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @Around("borrowingRecordControllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); // Proceed with method execution
        long executionTime = System.currentTimeMillis() - start;
        log.info("Method {} executed in {} ms", joinPoint.getSignature().getName(), executionTime);
        return proceed;
    }

    @AfterReturning(value = "borrowingRecordControllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned with value: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(value = "borrowingRecordControllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Method {} threw exception: {}", joinPoint.getSignature().getName(), exception.getMessage());
    }
}
