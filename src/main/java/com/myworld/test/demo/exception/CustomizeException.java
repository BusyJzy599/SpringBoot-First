package com.myworld.test.demo.exception;

/**
 *
 */
public class CustomizeException extends RuntimeException{

    private String message; //错误信息
    private Integer code;   //错误代码

    //构造
    public CustomizeException(ICustomizeErrorCode iCustomizeErrorCode){
        this.code=iCustomizeErrorCode.getCode();
        this.message=iCustomizeErrorCode.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
