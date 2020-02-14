package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.AccessTokenDTO;
import com.myworld.test.demo.dto.GithubUser;
import com.myworld.test.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

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
                           HttpServletRequest request){
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
        GithubUser user = githubProvider.getUser(accessToken);

        if(user!=null){
            //登陆成功,写session和cookie
            request.getSession().setAttribute("user",user);//把user对象放在session里面
            return "redirect:/";//用redirect将前缀地址删掉并且重定向到index页面
        }else{
            //登陆失败
            return "redirect:/";
        }
    }
}
