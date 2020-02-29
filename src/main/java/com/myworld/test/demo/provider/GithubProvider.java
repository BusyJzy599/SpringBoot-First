package com.myworld.test.demo.provider;

import com.alibaba.fastjson.JSON;
import com.myworld.test.demo.dto.AccessTokenDTO;
import com.myworld.test.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class GithubProvider {
    //获取GitHub的AccessToken
    public  String getAccessToken(AccessTokenDTO accessTokenDTO) {
        //okhttp的方法
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            System.out.println(string);//打印一下传回的数据字符串
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("请求超时");
        }
        return null;
    }

      //获取用户信息
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string= response.body().string();
            //JSON将string转成一个GithubUser对象
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) { }
        return null;
    }
}
