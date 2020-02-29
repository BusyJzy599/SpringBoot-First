package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.PaginationDTO;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.NotificationService;
import com.myworld.test.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action")String action,
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "5")Integer size,
                          Model model){
        //------------------------------------
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        if("questions".equals(action)){
            //我的提问面板
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO pagination=questionService.myList(user.getAccountId(),page,size);
            model.addAttribute("pagination",pagination);
        }else if("replies".equals(action)){
            //我的回复面板
          /*  Integer unreadCount=notificationService.unreadCount(user.getAccountId());
            model.addAttribute("unreadCount",unreadCount);*/
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","我的回复");
            PaginationDTO paginationDTO=notificationService.list(user.getAccountId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }
        return "profile";
    }

}
