package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.dto.QuestionDTO;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;//注入

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //根目录
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "5")Integer size){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {//判断cookie是否为空
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //如果存在则写入session
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //查询数据列表
        PaginationDTO pagination=questionService.list(page,size);
        //添加到model里面
        model.addAttribute("pagination",pagination);

        return "index";
    }

}
