package com.hair.management.bean.response;

/**
 * 返回结果累
 * @param <T>
 */
public class  ApiResult<T> {
    public Integer code;

    public T data;

    public static final Integer ERROR=500;
    public static final Integer SUCCESS=0;

    public ApiResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T t ){
        return new ApiResult<>(ApiResult.SUCCESS,t);
    }
    public static <T> ApiResult<T> success(){
        return new ApiResult<>(ApiResult.SUCCESS,null);
    }

    public static <T> ApiResult<T> error(T t){
        return new ApiResult<>(ApiResult.ERROR,t);
    }
    public static <T> ApiResult<T> error(){
        return new ApiResult<>(ApiResult.ERROR,null);
    }
}
