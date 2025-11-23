package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.CampusActivity;

public interface ICampusActivityService extends IService<CampusActivity> {
    // 定义文档要求的特殊业务方法
    boolean publishActivity(Integer id); // 活动发布 [cite: 43]
}