package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.AccessTokenDTO;
import com.myworld.test.demo.dto.GithubUser;
import com.myworld.test.demo.mapper.UserMapper;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.*;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    //用注解接受两个字符串参数
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //new一个AccessToken对象
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        //GitHub授权
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        //获取GitHub的access
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //通过access获取用户对象
        GithubUser githubUser = githubProvider.getUser(accessToken);

        if(githubUser!=null){
            User user=new User();
            //token作为识别对象
            String token=UUID.randomUUID().toString();
            //set用户属性
            user.setName(githubUser.getName());
            user.setToken(token);
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setBio(githubUser.getBio());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            System.out.println(user.getName());
            //插入数据库
            userMapper.insert(user);
            //将token标识放入cookie里面
            response.addCookie(new Cookie("token",token));
            //登陆成功,写session和cookie

            /**
            //把user对象放在session里面
            request.getSession().setAttribute("user",githubUser);
            //用redirect将前缀地址删掉并且重定向到index页面
             */
            return "redirect:/";
        }else{
            //登陆失败
            return "redirect:/";
        }
    }
}
