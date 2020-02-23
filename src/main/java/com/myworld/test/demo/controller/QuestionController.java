package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.CommentDTO;
import com.myworld.test.demo.dto.QuestionDTO;
import com.myworld.test.demo.enums.CommentTypeEnum;
import com.myworld.test.demo.service.CommentService;
import com.myworld.test.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 *
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public  String question(
            @PathVariable(name = "id")Integer id,
            Model model
            ){
        QuestionDTO questionDTO= questionService.getById(id);
        List<QuestionDTO>relatedQuestions=questionService.selectRelated(questionDTO);
        List<CommentDTO> comments =commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);

        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }


}

