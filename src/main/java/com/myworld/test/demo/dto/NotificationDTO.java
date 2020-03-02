package com.myworld.test.demo.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private int id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier;
    private String notifierName;
    private String outerTitle;
    private Integer outerid;
    private String typeName;
    private Integer type;
}
