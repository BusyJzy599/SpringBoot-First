package com.myworld.test.demo.service;

import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.dto.QuestionDTO;
import com.myworld.test.demo.mapper.QuestionMapper;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.Question;
import com.myworld.test.demo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 组装user和question
 */
@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;//注入

    @Autowired
    private QuestionMapper questionMapper;


    public PaginationDTO list(Integer page, Integer size) {
        //偏移量值
        Integer offset=size*(page-1);

        List <Question> questions=questionMapper.list(offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            //通过question的creator获取user对象
            User user=userMapper.findById(question.getCreator());

            //new一个新的DTO
            QuestionDTO questionDTO = new QuestionDTO();

            //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);

            //add进list
            questionDTOList.add(questionDTO);
        }
        //给paginationDTO设置属性
        paginationDTO.setQuestions(questionDTOList);
        //问题总数
        Integer totalCount = questionMapper.count();

        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }


    public PaginationDTO myList(Integer userId, Integer page, Integer size) {
        //偏移量值
        Integer offset=size*(page-1);

        List <Question> questions=questionMapper.myList(userId,offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            //通过question的creator获取user对象
            User user=userMapper.findById(question.getCreator());

            //new一个新的DTO
            QuestionDTO questionDTO = new QuestionDTO();

            //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);

            //add进list
            questionDTOList.add(questionDTO);
        }
        //给paginationDTO设置属性
        paginationDTO.setQuestions(questionDTOList);
        //问题总数
        Integer totalCount = questionMapper.countByUserId(userId);

        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;

    }

    public QuestionDTO getById(Integer id) {
        Question question=questionMapper.getById(id);
        //new一个新的DTO
        QuestionDTO questionDTO = new QuestionDTO();
        //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
        BeanUtils.copyProperties(question,questionDTO);
        //添加user
        User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(user);

        return questionDTO;

    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateQuestion(question);
        }
    }
}
