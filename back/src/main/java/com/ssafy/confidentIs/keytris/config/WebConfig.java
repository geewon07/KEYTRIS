package com.ssafy.confidentIs.keytris.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "http://j9a401.p.ssafy.io", "http://j9a401.p.ssafy.io:8080")
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowCredentials(false)
                .allowedOriginPatterns("")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
