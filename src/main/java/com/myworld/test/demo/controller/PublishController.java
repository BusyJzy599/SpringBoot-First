package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.QuestionDTO;
import com.myworld.test.demo.mapper.QuestionMapper;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.Question;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            @RequestParam("id")Integer id,
            HttpServletRequest request,
            Model model
    ){
        //new一个问题对象
        Question question = new Question();
        //设置相关参数
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);

        //存入model
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            //未登录则被拦截
            return "redirect:/";
        }else {
            //登录则返回当前

            //----------------------------
            //设置creator
            question.setCreator(user.getAccountId());
            question.setId(id);

            questionService.createOrUpdate(question);

            //---------------------------
            return "redirect:/";
        }
    }


    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id")Integer id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        //存入model
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());

        return "redirect:/";

    }

}
