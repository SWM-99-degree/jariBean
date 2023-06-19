package com.example.jariBean.handler.aop;

import com.example.jariBean.handler.ex.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CustomValidationAdvice {
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {}


    // @Before: joinPoint 의 전 제어
    // @After:  joinPoint 의 후 제어
    // @Around: joinPoint 의 전후 제어
    @Around("postMapping() || putMapping()")
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // joinPoint 의 매개 변수
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            if(arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                if(bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();
                    bindingResult.getFieldErrors().forEach(error -> {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    });
                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
        }
        return proceedingJoinPoint.proceed(); // 정상적으로 해당 메서드 실행
    }
}
