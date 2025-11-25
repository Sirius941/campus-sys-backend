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
                        "/user/login",      // 登录接口
                        "/user/add",        // 注册接口
                        "/doc.html",        // Knife4j 主页
                        "/webjars/**",      // 静态资源
                        "/v3/api-docs/**",  // OpenAPI 描述 json
                        "/favicon.ico"      // 图标
                );
    }
}