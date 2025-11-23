package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.TeachingEvaluation;
import java.util.List;

public interface ITeachingEvaluationService extends IService<TeachingEvaluation> {
    /**
     * 提交评价
     */
    boolean addEvaluation(TeachingEvaluation evaluation);

    /**
     * 查询某门课程下的所有评价
     */
    List<TeachingEvaluation> getEvaluationsByCourseId(Integer courseId);
}