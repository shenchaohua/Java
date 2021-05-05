package com.lagou;
import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.io.Reader;
import java.io.PrintWriter;
public class GenericTest extends GenericServlet {
    @java.lang.Override
    public void init() throws ServletException {
        super.init();
    }

    @java.lang.Override
    public void service(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
        Reader reader = servletRequest.getReader();
        char[] a =  new char[50];
        System.out.println(reader.read(a));
        System.out.println(Arrays.toString(a));
        PrintWriter pw = servletResponse.getWriter();
        pw.write("中文....");
        Map parameterMap = servletRequest.getParameterMap();
        parameterMap.forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
    }
}
