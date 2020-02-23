package com.myworld.test.demo.mapper;

import com.myworld.test.demo.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
    List<Question>selectRelated(Question question);
}