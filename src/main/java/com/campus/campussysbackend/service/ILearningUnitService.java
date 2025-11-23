package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.LearningUnit;
import java.util.List;

public interface ILearningUnitService extends IService<LearningUnit> {
    /**
     * 根据课程ID查询学习单元列表（通常需要按照章节层级排序或组装）
     */
    List<LearningUnit> getUnitsByCourseId(Integer courseId);
}