package com.example.tuantuan.Exception;

/**
 * 自定义业务逻辑异常类
 *
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
