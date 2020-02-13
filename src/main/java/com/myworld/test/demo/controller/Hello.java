package com.myworld.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
public class Hello {
    @GetMapping("/") //根目录
    public String hello(){
        return "index";
    }

}
