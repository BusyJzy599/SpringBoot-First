package com.myworld.test.demo.mapper;

import com.myworld.test.demo.model.Question;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
}