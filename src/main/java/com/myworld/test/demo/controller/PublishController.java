package com.myworld.test.demo.controller;

import com.myworld.test.demo.mapper.QuestionMapper;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.Question;
import com.myworld.test.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
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

        User user = null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            //判断cookie是否为空
            for (Cookie cookie : cookies) {
                String token = cookie.getValue();
                user=userMapper.findByToken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }

                question.setCreator(user.getId());
                question.setGmtCreate(System.currentTimeMillis());
                question.setGmtModified(question.getGmtCreate());
                questionMapper.create(question);
                return "redirect:/";

        }else {
            model.addAttribute("error","用户未登录");
            return "/publish";
        }

    }

}
