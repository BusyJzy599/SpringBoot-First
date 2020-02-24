package com.myworld.test.demo.cache;

import com.myworld.test.demo.dto.TagDTO;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class TagCache {

    public static List<TagDTO>get(){
        List<TagDTO>tagDTOS=new ArrayList<>();
        //开发语言板块
        TagDTO program=new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","java","python"));
        tagDTOS.add(program);
        //平台框架板块
        TagDTO frameWork = new TagDTO();
        frameWork.setCategoryName("平台框架");
        frameWork.setTags(Arrays.asList("Spring","MVC","Vue"));
        tagDTOS.add(frameWork);
        //服务器板块
        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("Windows","linux","apache"));
        tagDTOS.add(server);

        return tagDTOS;
    }

}
