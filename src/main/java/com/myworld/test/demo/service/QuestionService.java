package com.myworld.test.demo.service;

import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.dto.QuestionDTO;
import com.myworld.test.demo.exception.CustomizeErrorCode;
import com.myworld.test.demo.exception.CustomizeException;
import com.myworld.test.demo.mapper.QuestionExtMapper;
import com.myworld.test.demo.mapper.QuestionMapper;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.Question;
import com.myworld.test.demo.model.QuestionExample;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.model.UserExample;
import org.apache.ibatis.session.RowBounds;
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
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionMapper questionMapper;


    public PaginationDTO list(Integer page, Integer size) {
        //偏移量值
        Integer offset=size*(page-1);

        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        //List <Question> questions=questionMapper.list(offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            //通过question的creator获取user对象
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            //User user=userMapper.findById(question.getCreator());

            //new一个新的DTO
            QuestionDTO questionDTO = new QuestionDTO();

            //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(users.get(0));

            //add进list
            questionDTOList.add(questionDTO);
        }
        //给paginationDTO设置属性
        paginationDTO.setQuestions(questionDTOList);
        //问题总数
        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());
        //Integer totalCount = questionMapper.count();

        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;
    }


    public PaginationDTO myList(Integer userId, Integer page, Integer size) {
        //偏移量值
        Integer offset=size*(page-1);

        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offset, size));
        //List <Question> questions=questionMapper.myList(userId,offset,size);
        List<QuestionDTO>questionDTOList=new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {
            //通过question的creator获取user对象
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            //User user=userMapper.findById(question.getCreator());

            //new一个新的DTO
            QuestionDTO questionDTO = new QuestionDTO();

            //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(users.get(0));

            //add进list
            questionDTOList.add(questionDTO);
        }
        //给paginationDTO设置属性
        paginationDTO.setQuestions(questionDTOList);
        //问题总数
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);
        //Integer totalCount = questionMapper.countByUserId(userId);

        paginationDTO.setPagination(totalCount,page,size);

        return paginationDTO;

    }

    public QuestionDTO getById(Integer id) {

        Question question= questionMapper.selectByPrimaryKey(id);

       if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        //Question question=questionMapper.getById(id);
        //new一个新的DTO
        QuestionDTO questionDTO = new QuestionDTO();
        //set到questionDTO里去,快速把第一个对象的属性拷贝到第二个对象里面(Spring boot内置函数)
        BeanUtils.copyProperties(question,questionDTO);
        //添加user
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(userExample);
        //User user=userMapper.findById(question.getCreator());
        questionDTO.setUser(users.get(0));

        return questionDTO;

    }

    public void createOrUpdate(Question question) {
        question.setCommentCount(0);
        question.setLikeCount(0);
        question.setViewCount(0);
        question.setCommentCount(0);
        if(question.getId()==null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{
            //更新
            question.setGmtModified(System.currentTimeMillis());

           int updated= questionMapper.updateByPrimaryKeySelective(question);
           if(updated!=1)
               throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
    }

    public void incView(Integer id) {
       /* Question question = questionMapper.selectByPrimaryKey(id);
        Question updateQuestion=new Question();
        updateQuestion.setViewCount(question.getViewCount()+1);
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andIdEqualTo(id);
*/
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
       /* questionMapper.updateByExampleSelective(updateQuestion,questionExample);*/
    }
}
