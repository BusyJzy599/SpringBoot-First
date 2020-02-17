package com.myworld.test.demo.mapper;

import com.myworld.test.demo.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator) " +
            "values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator})")
    void create(Question question);
}
