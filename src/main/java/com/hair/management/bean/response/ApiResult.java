package com.hair.management.bean.response;

import java.io.Serializable;

/**
 * 返回结果累
 * @param <T>
 */
public class  ApiResult<T> implements Serializable {
    public Integer code;
    public String msg;
    public T data;

    public static final Integer ERROR=500;
    public static final Integer SUCCESS=0;

    public ApiResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T t ){
        return new ApiResult<>(ApiResult.SUCCESS,null,t);
    }
    public static <T> ApiResult<T> success(){
        return new ApiResult<>(ApiResult.SUCCESS,null,null);
    }

    public static <T> ApiResult<T> error(String msg){
        return new ApiResult<>(ApiResult.ERROR,msg,null);
    }
    public static <T> ApiResult<T> error(int code,String msg){
        return new ApiResult<>(code,msg,null);
    }
}
