package com.example.JavaEEServletDemo;

import java.io.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

//@WebServlet("/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(Thread.currentThread().getName());
        response.setContentType("text/html");
        int sum=0;
//        for(int i=1;i<2000000000;i++) {
//            sum += i;
//            sum /= i;
//            sum -= i/2;
//        }
        message = String.valueOf(sum) + "1";
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}