package com.myworld.test.demo.exception;

/**
 *
 */
public class CustomizeException extends RuntimeException{

    private String message;
    private Integer code;


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
