package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.Course;

public interface ICourseService extends IService<Course> {
    // 基础创建逻辑使用 Mybatis-Plus 自带的 save 即可，无需额外定义接口
}