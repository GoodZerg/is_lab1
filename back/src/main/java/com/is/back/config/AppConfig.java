package com.is.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class AppConfig {

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        //resolver.setMaxUploadSize(10485760); // 10 MB
        //resolver.setDefaultEncoding("utf-8");
        return resolver;
    }
}
