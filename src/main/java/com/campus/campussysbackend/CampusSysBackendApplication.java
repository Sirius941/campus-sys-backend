package com.campus.campussysbackend; // 保持你的包名不变

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 【关键修改】这里要改成你的实际包路径 + .mapper
@MapperScan("com.campus.campussysbackend.mapper")
public class CampusSysBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusSysBackendApplication.class, args);
    }

}
