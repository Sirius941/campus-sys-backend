package com.campus.campussysbackend.config;

import com.campus.campussysbackend.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns(
                        "/user/login",      // 排除登录接口 [cite: 67]
                        "/user/add",        // 排除注册(新增用户)接口
                        "/student/add",     // 如果允许未登录报名，需排除；否则需登录后录入
                        "/doc.html",        // 排除 Swagger/Knife4j 文档资源
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"
                );
    }
}