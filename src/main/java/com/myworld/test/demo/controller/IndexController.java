package com.myworld.test.demo.controller;

import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;//注入

    @GetMapping("/") //根目录
    public String index(HttpServletRequest request){
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
        return "index";
    }

}
