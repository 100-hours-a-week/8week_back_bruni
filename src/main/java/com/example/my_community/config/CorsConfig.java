package com.example.my_community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 경로에만 CORS 허용 (원하면 "/**")
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5500" // Live Server가 이 주소를 쓸 때 대비
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)       // ★ 쿠키/자격증명 허용
                .maxAge(3600);                // preflight 캐시(초)
    }
}
