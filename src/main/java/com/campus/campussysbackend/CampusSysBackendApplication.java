package com.campus.campussysbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // 引入这个包

// 【关键修改】 exclude = {SecurityAutoConfiguration.class}
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.campus.campussysbackend.mapper")
public class CampusSysBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusSysBackendApplication.class, args);
    }

}