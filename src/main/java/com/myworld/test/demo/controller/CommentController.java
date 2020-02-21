package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.CommentDTO;
import com.myworld.test.demo.dto.ResultDTO;
import com.myworld.test.demo.exception.CustomizeErrorCode;
import com.myworld.test.demo.model.Comment;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){
       /* User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }*/

        Comment comment=new Comment();
        //set评论的相关属性
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        /*comment.setCommentator(user.getAccountId());*/
        comment.setCommentator(1);
        comment.setLikeCount(0);

        commentService.insert(comment);

        return ResultDTO.okOf();
    }
}
