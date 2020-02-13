package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.AccessTokenDTO;
import com.myworld.test.demo.dto.GithubUser;
import com.myworld.test.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @GetMapping("/callback")
    //用注解接受两个字符串参数
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        //new一个AccessToken对象
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //GitHub授权
        accessTokenDTO.setClient_id("6cadd4ad60f1467acede");
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setClient_secret("bfcc3794a7454242429fb290663f7cf916fb45be");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        //抓取这个对象
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println("user_name:"+user.getName());
        return "index";
    }
}
