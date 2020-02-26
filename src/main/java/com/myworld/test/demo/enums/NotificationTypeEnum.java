package com.myworld.test.demo.enums;

/**
 * 通知类型
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论")
    ;
    private Integer type;
    private String name;

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    NotificationTypeEnum(Integer status, String name) {
        this.type = status;
        this.name = name;
    }
    public static String nameOfType(Integer type){
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if(notificationTypeEnum.getType()==type){
                return notificationTypeEnum.getName();
            }
        }
        return "";
    }
}
