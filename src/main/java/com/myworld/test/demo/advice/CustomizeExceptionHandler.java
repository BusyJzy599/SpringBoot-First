package com.myworld.test.demo.advice;

import com.alibaba.fastjson.JSON;
import com.myworld.test.demo.dto.ResultDTO;
import com.myworld.test.demo.exception.CustomizeErrorCode;
import com.myworld.test.demo.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)      //抓取异常
    Object handle(Throwable ex,
                  Model model,              //用于添加错误页面信息
                  HttpServletRequest request,
                  HttpServletResponse response){

        String contentType=request.getContentType();
        if("application/json".equals(contentType)){
                ResultDTO resultDTO;
            //返回json
            if(ex instanceof CustomizeException){
                resultDTO= ResultDTO.errorOf((CustomizeException)ex);
            }else {
                resultDTO=ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                //进行输出流的传输
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                //将resultDTO写到前端
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e) {}
            return null;
        }else {
            //错误页面跳转
            if(ex instanceof CustomizeException){
                model.addAttribute("message",ex.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            //返回error页面
            return new ModelAndView("error");
        }
    }
}
