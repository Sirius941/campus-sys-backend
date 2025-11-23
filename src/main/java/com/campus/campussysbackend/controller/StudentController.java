package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.Student;
import com.campus.campussysbackend.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    /**
     * 1. 新增学员 [cite: 62]
     * 自动创建关联账号，状态默认为未审核
     */
    @PostMapping("/add")
    public Result<Boolean> addStudent(@RequestBody Student student) {
        if (!StringUtils.hasText(student.getParentPhone())) {
            return Result.error("监护人电话不能为空（将作为登录账号）");
        }
        try {
            studentService.addStudentWithUser(student);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("新增学员失败：" + e.getMessage());
        }
    }

    /**
     * 2. 学员查询 [cite: 61]
     * 支持按姓名模糊查询
     */
    @GetMapping("/list")
    public Result<List<Student>> listStudents(@RequestParam(required = false) String name) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Student::getStuName, name);
        }
        // 按 ID 降序
        wrapper.orderByDesc(Student::getId);
        return Result.success(studentService.list(wrapper));
    }

    /**
     * 3. 审核学员 [cite: 64]
     * 将关联账号状态改为正常
     */
    @PutMapping("/audit/{id}")
    public Result<Boolean> auditStudent(@PathVariable Integer id) {
        try {
            studentService.auditStudent(id);
            return Result.success(true);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 4. 批量导入学员 (Excel导入) [cite: 63]
     * 这是一个接口定义的占位符。
     * 真正实现需要引入 EasyExcel 或 POI 依赖来解析文件。
     */
    @PostMapping("/import")
    public Result<String> importStudents() {
        // TODO: 1. 接收 MultipartFile file
        // TODO: 2. 使用 EasyExcel 读取数据
        // TODO: 3. 循环调用 studentService.addStudentWithUser(student)
        return Result.success("导入功能需引入Excel依赖库实现，接口已预留");
    }
    /**
     * 5. 批量审核学员 [cite: 65]
     * 接口地址：PUT /student/audit/batch
     * 请求体：[1, 2, 3, 4]  (JSON 数组)
     */
    @PutMapping("/audit/batch")
    public Result<Boolean> auditStudentsBatch(@RequestBody List<Integer> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return Result.error("请选择至少一名学员进行审核");
        }
        try {
            studentService.auditStudentsBatch(studentIds);
            return Result.success(true);
        } catch (RuntimeException e) {
            return Result.error("批量审核失败：" + e.getMessage());
        }
    }
}