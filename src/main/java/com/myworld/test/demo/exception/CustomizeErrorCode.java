package com.myworld.test.demo.exception;

/**
 *封装错误代码和错误信息
 */
public enum CustomizeErrorCode implements  ICustomizeErrorCode{


    QUESTION_NOT_FOUND(2001,"你找的问题不在啦!!!"),
    TARGET_PARAM_NOT_FOUND(2001,"未选中任何问题或评论进行回复!!!"),
    NO_LOGIN(2003,"请登录后进行评论!"),
    SYSTEM_ERROR(2004,"服务冒烟了，请稍后再试试!!!"),
    TYPE_PARAM_WRONG(2005,"错误或不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在了"),
    COMMENT_IS_EMPTY(2007,"输入内容不能为空!"),
    ;

    private Integer code;
    private String message;

    @Override
    public String getMessage(){ return message; }

    @Override
    public Integer getCode() {
        return code;
    }

    //构造
    CustomizeErrorCode(Integer code, String message){
        this.message=message;
        this.code=code;
    }

}
