package com.myworld.test.demo.model;

import lombok.Data;

/**
 *
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private int creator;
    private String tag;

}
