package com.hair.management.controller;

import com.hair.management.bean.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ApiResult<String> exceptionHandler(RuntimeException runtimeException) {
        log.error("exception:", runtimeException);
        return ApiResult.error(runtimeException.getMessage());
    }
    @ExceptionHandler(value = UnauthorizedException.class)
    public ApiResult<String> shiroExceptionHandler(UnauthorizedException shiroException){
        return ApiResult.error(401,"未授权");
    }
}
