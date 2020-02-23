package com.myworld.test.demo.dto;

import com.myworld.test.demo.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private String content;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer commentCount;
    private Integer commentator;
    private Integer type;
    private Integer likeCount;
    private User user;
}
