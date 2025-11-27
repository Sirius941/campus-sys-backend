package com.campus.campussysbackend.controller;

import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.TeachingEvaluation;
import com.campus.campussysbackend.service.ITeachingEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "教学评价管理")
@RestController
@RequestMapping("/evaluation")
public class TeachingEvaluationController {

    @Autowired
    private ITeachingEvaluationService evaluationService;

    /**
     * 1. 提交评价 (教师对学生的学习单元评价/课程总评)
     * [cite: 60]
     */
    @Operation(summary = "提交教学评价")
    @PostMapping("/add")
    public Result<Boolean> addEvaluation(@RequestBody TeachingEvaluation evaluation) {
        try {
            boolean success = evaluationService.addEvaluation(evaluation);
            return Result.success(success);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 2. 查询评价列表 (按课程)
     * 方便教师查看自己已评价的记录
     */
    @GetMapping("/list/{courseId}")
    public Result<List<TeachingEvaluation>> listEvaluations(@PathVariable Integer courseId) {
        return Result.success(evaluationService.getEvaluationsByCourseId(courseId));
    }
}