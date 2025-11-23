package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.TeachingClass;
import com.campus.campussysbackend.entity.TeachingStudentInClass;
import com.campus.campussysbackend.service.ITeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.campus.campussysbackend.service.ITeachingStudentInClassService;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ITeachingClassService classService;

    /**
     * 1. 教学班创建
     * 文档要求：对每个活动，可创建一个或多个教学班，必须关联已发布的活动 [cite: 46]
     */
    @PostMapping("/create")
    public Result<Boolean> createClass(@RequestBody TeachingClass teachingClass) {
        try {
            // 调用包含业务校验的 Service 方法
            boolean success = classService.createClass(teachingClass);
            return Result.success(success);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 2. 教学班修改
     * [cite: 47]
     */
    @PutMapping("/update")
    public Result<Boolean> updateClass(@RequestBody TeachingClass teachingClass) {
        return Result.success(classService.updateById(teachingClass));
    }

    /**
     * 3. 配置教师
     * 文档要求：针对创建的教学班，可以配置对应的教师 [cite: 49]
     * 实现：本质上就是更新 teacherId 字段
     */
    @PutMapping("/assign-teacher")
    public Result<Boolean> assignTeacher(@RequestParam Integer classId,
                                         @RequestParam Integer teacherId) {
        TeachingClass teachingClass = new TeachingClass();
        teachingClass.setId(classId);
        teachingClass.setTeacherId(teacherId);
        return Result.success(classService.updateById(teachingClass));
    }

    /**
     * 4. 教学班查询
     * 通常需要支持：按活动ID查（看这个活动下有哪些班）、按班级名查
     */
    @GetMapping("/list")
    public Result<List<TeachingClass>> listClasses(
            @RequestParam(required = false) Integer activityId,
            @RequestParam(required = false) String name) {

        LambdaQueryWrapper<TeachingClass> wrapper = new LambdaQueryWrapper<>();

        // 如果前端传了 activityId，就查该活动下的班级
        if (activityId != null) {
            wrapper.eq(TeachingClass::getActivityId, activityId);
        }

        // 模糊查询班级名
        if (StringUtils.hasText(name)) {
            wrapper.like(TeachingClass::getName, name);
        }

        // 按创建时间倒序
        wrapper.orderByDesc(TeachingClass::getCreatedTime);

        return Result.success(classService.list(wrapper));
    }

    /**
     * 5. 根据ID获取班级详情
     */
    @GetMapping("/{id}")
    public Result<TeachingClass> getClassById(@PathVariable Integer id) {
        return Result.success(classService.getById(id));
    }
    @Autowired
    private ITeachingStudentInClassService classStudentService; // 注入新的 Service

    /**
     * 2.1.2 关联学员 (批量添加学员到班级)
     * 接口地址：POST /class/add-students
     * 请求参数示例：
     * {
     * "classId": 101,
     * "studentIds": [1, 2, 3, 5]
     * }
     */
    @PostMapping("/add-students")
    public Result<Boolean> addStudentsToClass(@RequestBody Map<String, Object> params) {
        try {
            // 1. 解析参数
            Integer classId = (Integer) params.get("classId");
            List<Integer> studentIds = (List<Integer>) params.get("studentIds");

            if (classId == null || studentIds == null) {
                return Result.error("参数错误：classId 和 studentIds 必填");
            }

            // 2. 调用 Service
            classStudentService.addStudentsToClass(classId, studentIds);
            return Result.success(true);

        } catch (Exception e) {
            return Result.error("添加学员失败：" + e.getMessage());
        }
    }

    /**
     * (可选补充) 查询某个班级下的所有学员
     * 这对前端展示班级详情很有用
     */
    @GetMapping("/{classId}/students")
    public Result<List<TeachingStudentInClass>> getClassStudents(@PathVariable Integer classId) {
        LambdaQueryWrapper<TeachingStudentInClass> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeachingStudentInClass::getClassId, classId);
        return Result.success(classStudentService.list(wrapper));
    }
}