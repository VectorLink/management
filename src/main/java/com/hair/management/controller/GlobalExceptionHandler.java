package com.hair.management.controller;

import com.hair.management.bean.response.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ApiResult<String> exceptionHandler(Throwable throwable) {
        return ApiResult.error(throwable.getMessage());
    }
}
