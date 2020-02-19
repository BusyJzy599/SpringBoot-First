package com.myworld.test.demo.advice;

import com.myworld.test.demo.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(/*HttpServletRequest request,*/
                        Throwable ex,
                        Model model){
       // HttpStatus status=getStatus(request);

        if(ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else {
            model.addAttribute("message","服务冒烟了，请稍后再试试!!!");
        }
        return new ModelAndView("error");
    }

   /* private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode=(Integer)request.getAttribute("java.servlet,error,status_code");
        if(statusCode==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
*/
}
