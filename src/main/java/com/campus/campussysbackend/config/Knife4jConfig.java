package com.campus.campussysbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学习营活动管理平台后台接口文档")
                        .version("1.0")
                        .description("Campus System Backend API")
                        .contact(new Contact().name("Admin").email("admin@campus.com")));
    }
}