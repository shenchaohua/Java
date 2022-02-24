package com.czl.demo.filter;

import org.springframework.http.MediaType;
import org.springframework.web.cors.reactive.PreFlightRequestHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "AppFilter", urlPatterns = "/*")
public class Myfilter implements Filter {

        @Override
        public void destroy() {
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;

            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            ArrayList<String> allowOrigins = (ArrayList<String>) req.getServletContext().getAttribute("allowOrigins");
            String origin = request.getHeader("Origin");
            if (allowOrigins.contains(origin)) {
                response.setHeader("Access-Control-Allow-Origin", origin);
            }
            // Access-Control-Max-Age
            response.setHeader("Access-Control-Max-Age", "3600");
            // Access-Control-Allow-Credentials
            response.setHeader("Access-Control-Allow-Credentials", "true");
            // Access-Control-Allow-Methods
            response.setHeader("Access-Control-Allow-Methods", "PUT,POST, GET, OPTIONS, DELETE");

            response.setHeader("Access-Control-Allow-Headers", "Content-Type");

            chain.doFilter(req, resp);
        }

        @Override
        public void init(FilterConfig config) throws ServletException {
            //白名单
            ArrayList<String> allowOrigins = new ArrayList<>();
            allowOrigins.add("http://localhost:63343");
            allowOrigins.add("http://localhost:9528");
            config.getServletContext().setAttribute("allowOrigins", allowOrigins);
            AbstractHandlerMapping
        }
}
