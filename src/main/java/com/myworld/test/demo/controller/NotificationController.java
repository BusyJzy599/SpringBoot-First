package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.NotificationDTO;
import com.myworld.test.demo.enums.NotificationTypeEnum;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * 通知控制类
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id")Integer id,
                          Model model){
        //------------------------------------
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO= notificationService.read(id,user);

        if(NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()){
            //回复评论
            return "redirect:/question/"+notificationDTO.getOuterid();
        }
        if(NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            //回复问题
            return "redirect:/question/"+notificationDTO.getOuterid();
        }
        return "redirect:/";
    }
}
