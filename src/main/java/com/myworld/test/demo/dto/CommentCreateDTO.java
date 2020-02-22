package com.myworld.test.demo.dto;

import lombok.Data;

/**
 * 回复类
 */
@Data
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;

}
