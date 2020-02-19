package com.myworld.test.demo.exception;

/**
 *
 */
public enum CustomizeErrorCode implements  ICustomizeErrorCode{


    QUESTION_NOT_FOUND("\"你找的问题不在啦!!!\"");
    private String message;

    @Override
    public String getMessage(){
        //重写方法赋值message
        return message;
    }

    CustomizeErrorCode(String message){
        this.message=message;
    }
}
