package com.atguigu.gmall.admin.config;

import com.atguigu.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Slf4j
@Aspect
@Component
public class GmallValidatorAspect {

    @Around("execution(* com.atguigu.gmall.admin..controller..*.*(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("校验切面切入工作");
        Object[] args = proceedingJoinPoint.getArgs();
        Object proceed = null;
        for (Object obj:args) {
            if(obj instanceof BindingResult){
                int errorCount = ((BindingResult) obj).getErrorCount();
                if(errorCount>0){
                    //有错误
                    log.info("校验发生了错误");
                    CommonResult commonResult = new CommonResult().validateFailed((BindingResult) obj);
                    return commonResult;
                }
            }
        }

        proceed = proceedingJoinPoint.proceed(args);


    return proceed;
    }

}
