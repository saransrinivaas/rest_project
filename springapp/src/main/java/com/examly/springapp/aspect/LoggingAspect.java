package com.examly.springapp.aspect;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import com.examly.springapp.logging.loggingService;

@Aspect
@Component
public class LoggingAspect {
    private final loggingService loggingService;

    public LoggingAspect(loggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Before("execution(* com.yourapp..*(..))") // Before any method in your project
    public void logBeforeMethod(JoinPoint joinPoint) {
        String message = "BEFORE method: " + joinPoint.getSignature().toShortString();
        loggingService.writeLog(message);
    }

    @After("execution(* com.yourapp..*(..))") // After any method in your project
    public void logAfterMethod(JoinPoint joinPoint) {
        String message = "AFTER method: " + joinPoint.getSignature().toShortString();
        loggingService.writeLog(message);
    }
}
