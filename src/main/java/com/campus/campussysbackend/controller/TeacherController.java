package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.Teacher;
import com.campus.campussysbackend.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;

    /**
     * 1. 新增教师
     * 文档要求 [cite: 17, 51]：添加教师时，自动添加对应的用户记录。
     * 注意：这里调用的是我们在 Service 层专门写的事务方法 addTeacherWithUser。
     */
    @PostMapping("/add")
    public Result<Boolean> addTeacher(@RequestBody Teacher teacher) {
        // 简单校验：教师姓名不能为空
        if (!StringUtils.hasText(teacher.getTeacherName())) {
            return Result.error("教师姓名不能为空");
        }

        try {
            // 调用包含“创建用户+创建教师”双重逻辑的事务方法
            teacherService.addTeacherWithUser(teacher);
            return Result.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加教师失败：" + e.getMessage());
        }
    }

    /**
     * 2. 根据ID查询教师
     * 文档要求 ：根据id查询教师信息
     */
    @GetMapping("/{id}")
    public Result<Teacher> getTeacherById(@PathVariable Integer id) {
        Teacher teacher = teacherService.getById(id);
        if (teacher == null) {
            return Result.error("未找到该教师");
        }
        return Result.success(teacher);
    }

    /**
     * 3. 教师列表查询 (筛选)
     * 文档要求 ：根据教师筛选条件查询教师
     * 支持条件：姓名(模糊)、学校(精确)、学科(精确)
     */
    @GetMapping("/list")
    public Result<List<Teacher>> listTeachers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String school,
            @RequestParam(required = false) String subject) {

        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();

        // 姓名支持模糊查询
        if (StringUtils.hasText(name)) {
            wrapper.like(Teacher::getTeacherName, name);
        }
        // 学校支持精确查询
        if (StringUtils.hasText(school)) {
            wrapper.eq(Teacher::getSchool, school);
        }
        // 学科支持精确查询
        if (StringUtils.hasText(subject)) {
            wrapper.eq(Teacher::getSubject, subject);
        }

        // 按照 ID 倒序排列（新添加的在前面）
        wrapper.orderByDesc(Teacher::getId);

        return Result.success(teacherService.list(wrapper));
    }

    /**
     * (可选) 更新教师信息
     * 文档虽然主要提了新增和查询，但通常管理后台都需要修改功能
     */
    @PutMapping("/update")
    public Result<Boolean> updateTeacher(@RequestBody Teacher teacher) {
        boolean success = teacherService.updateById(teacher);
        return Result.success(success);
    }

    /**
     * (可选) 删除教师
     * 实际上可能需要级联删除用户，或者逻辑删除，视具体需求而定
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteTeacher(@PathVariable Integer id) {
        return Result.success(teacherService.removeById(id));
    }
}