package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.Student;
import com.campus.campussysbackend.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;
import com.campus.campussysbackend.common.StudentImportListener;
import com.campus.campussysbackend.entity.StudentImportVO;
import java.io.IOException;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    /**
     * 1. 新增学员
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
     * 2. 学员查询
     */
    @GetMapping("/list")
    public Result<List<Student>> listStudents(@RequestParam(required = false) String name) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Student::getStuName, name);
        }
        wrapper.orderByDesc(Student::getId);
        return Result.success(studentService.list(wrapper));
    }

    /**
     * 3. 审核学员
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

    // 【删除】这里原来有一个占位符的 importStudents() 方法，必须删掉！

    /**
     * 5. 批量审核学员
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

    /**
     * 6. 批量导入学员 (Excel导入)
     * 实现逻辑：接收文件 -> 解析 Excel -> 循环入库
     */
    @PostMapping("/import")
    public Result<String> importStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            // 使用 EasyExcel 读取
            EasyExcel.read(file.getInputStream(), StudentImportVO.class, new StudentImportListener(studentService))
                    .sheet()
                    .doRead();

            return Result.success("导入成功（部分失败请查看后台日志）");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("导入失败：" + e.getMessage());
        }
    }
}