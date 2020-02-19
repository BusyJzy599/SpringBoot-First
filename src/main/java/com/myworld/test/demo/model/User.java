package com.myworld.test.demo.model;

import lombok.Data;

/**
 *
 */
@Data
public class User {
    private Integer id;
    private String name;
    private Integer accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String bio;
    private String avatarUrl;


}

