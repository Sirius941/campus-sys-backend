package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.TeachingClass;

public interface ITeachingClassService extends IService<TeachingClass> {
    /**
     * 创建教学班（包含业务校验：活动必须已发布）
     */
    boolean createClass(TeachingClass teachingClass);
}