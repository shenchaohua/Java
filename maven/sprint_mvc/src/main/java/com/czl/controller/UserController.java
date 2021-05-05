package com.czl.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class UserController {

    @RequestMapping("/index")
    public String index(){
        System.out.println("index page");
        return "/WEB-INF/pages/index.jsp";
    }

    @RequestMapping("/index2")
    public String index2(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write("拉勾网");
        request.setAttribute("username", "拉勾教育");
// 2.通过request实现转发
        request.getRequestDispatcher("/WEB-INF/pages/success.jsp").forward(request,
                response);
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return "ok";
    }
}
