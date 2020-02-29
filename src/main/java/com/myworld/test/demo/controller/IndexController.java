package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //根目录
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "5")Integer size){

        //查询数据列表
        PaginationDTO pagination=questionService.list(page,size);
        //添加到model里面
        model.addAttribute("pagination",pagination);

        return "index";
    }

}
