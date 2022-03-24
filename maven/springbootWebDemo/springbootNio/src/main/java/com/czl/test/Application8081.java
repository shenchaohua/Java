package com.czl.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application8081 {
    public static void main(String[] args) {
        SpringApplication.run(Application8081.class);
    }

    @Bean
    public RestTemplate getRestT() {
        return new RestTemplate();
    }
}
