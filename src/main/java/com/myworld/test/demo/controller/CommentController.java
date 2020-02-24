package com.myworld.test.demo.controller;

import com.myworld.test.demo.dto.CommentCreateDTO;
import com.myworld.test.demo.dto.CommentDTO;
import com.myworld.test.demo.dto.ResultDTO;
import com.myworld.test.demo.enums.CommentTypeEnum;
import com.myworld.test.demo.exception.CustomizeErrorCode;
import com.myworld.test.demo.model.Comment;
import com.myworld.test.demo.model.User;
import com.myworld.test.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;


    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentCreateDTO==null||commentCreateDTO.getContent().isEmpty()){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }

        Comment comment=new Comment();
        //set评论的相关属性
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getAccountId());
        comment.setCommentCount(0);
        comment.setLikeCount(0);

        commentService.insert(comment);

        return ResultDTO.okOf();
    }

    /*获取二级评论*/
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.POST)
    public ResultDTO comments(@PathVariable(name = "id")Integer id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
