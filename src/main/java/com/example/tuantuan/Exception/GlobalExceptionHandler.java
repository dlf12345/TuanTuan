package com.example.tuantuan.Exception;

import com.example.tuantuan.domain.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//定义一个全局异常处理器，捕获后端可能出现的所有异常，进行处理
//底层使用的是AOP编程，代理实现
//@RestControllerAdvice=@ControllerAdvice+@ResponseBody
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R<String> getException(Exception ex){
        if(ex.getMessage().contains("Duplicate entry")){
            return R.error("用户名已经存在，请重新输入");
        }
        return R.error("未知错误，请联系管理员");
    }
    //捕获自定义逻辑异常并处理
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
