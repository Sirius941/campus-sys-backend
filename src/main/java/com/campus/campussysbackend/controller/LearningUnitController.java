package com.campus.campussysbackend.controller;

import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.LearningUnit;
import com.campus.campussysbackend.service.ILearningUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/learning-unit")
public class LearningUnitController {

    @Autowired
    private ILearningUnitService learningUnitService;

    /**
     * b) 学习单元创建 [cite: 56]
     * 注意：如果是创建“小节”，前端需要传 fatherId；如果是“章”，fatherId 为空或0
     */
    @PostMapping("/create")
    public Result<Boolean> createUnit(@RequestBody LearningUnit unit) {
        // 简单校验
        if (unit.getCourseId() == null) {
            return Result.error("必须关联课程ID");
        }
        boolean success = learningUnitService.save(unit);
        return Result.success(success);
    }

    /**
     * b) 学习单元查询: 根据课程id,来查询对应的学习单元列表 [cite: 57, 58]
     */
    @GetMapping("/list/{courseId}")
    public Result<List<LearningUnit>> listUnitsByCourse(@PathVariable Integer courseId) {
        List<LearningUnit> list = learningUnitService.getUnitsByCourseId(courseId);
        return Result.success(list);
    }
}