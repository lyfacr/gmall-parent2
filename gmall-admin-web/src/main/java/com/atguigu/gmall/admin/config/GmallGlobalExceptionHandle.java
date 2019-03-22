package com.atguigu.gmall.admin.config;

import com.atguigu.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 处理抛出的异常
 */

//@ControllerAdvice //统一异常处理类
@Slf4j
@RestControllerAdvice
public class GmallGlobalExceptionHandle {

    @ExceptionHandler(ArithmeticException.class)
    public Object handleNullpointException(Exception e){
        log.error("全局异常处理类感知异常");
        return new CommonResult().failed().validateFailed(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object handle(Exception e){
        log.error("全局异常处理类感知异常");
        return new CommonResult().failed().validateFailed(e.getMessage());
    }
}
