package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.TeachingEvaluation;
import com.campus.campussysbackend.mapper.TeachingEvaluationMapper;
import com.campus.campussysbackend.service.ITeachingEvaluationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeachingEvaluationServiceImpl extends ServiceImpl<TeachingEvaluationMapper, TeachingEvaluation> implements ITeachingEvaluationService {

    @Override
    public boolean addEvaluation(TeachingEvaluation evaluation) {
        // 1. 校验必填项
        if (evaluation.getStuInClassId() == null || evaluation.getCourseId() == null || evaluation.getEvaluator() == null) {
            throw new RuntimeException("评价失败：学生、课程或教师信息缺失");
        }

        // 2. 自动补充评价时间
        evaluation.setEvaulatedTime(LocalDateTime.now());

        // 3. (可选) 查重逻辑：防止重复评价同一学生同一门课
        // LambdaQueryWrapper<TeachingEvaluation> query = new LambdaQueryWrapper<>();
        // query.eq(TeachingEvaluation::getStuInClassId, evaluation.getStuInClassId())
        //      .eq(TeachingEvaluation::getCourseId, evaluation.getCourseId());
        // if (this.count(query) > 0) { throw new RuntimeException("该学生已评价，请勿重复提交"); }

        return this.save(evaluation);
    }

    @Override
    public List<TeachingEvaluation> getEvaluationsByCourseId(Integer courseId) {
        LambdaQueryWrapper<TeachingEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachingEvaluation::getCourseId, courseId);
        wrapper.orderByDesc(TeachingEvaluation::getEvaulatedTime);
        return this.list(wrapper);
    }
}