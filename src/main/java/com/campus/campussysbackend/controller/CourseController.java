package com.campus.campussysbackend.controller;

import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.Course;
import com.campus.campussysbackend.service.ICourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Tag(name = "课程管理")
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private ICourseService courseService;

    /**
     * a) 课程创建 [cite: 54]
     */
    @Operation(summary = "创建课程")
    @PostMapping("/create")
    public Result<Boolean> createCourse(@RequestBody Course course) {
        // 补全默认字段
        course.setCreatedTime(LocalDateTime.now());
        if (course.getStatus() == null) {
            course.setStatus("0"); // 0表示已创建
        }

        boolean success = courseService.save(course);
        return Result.success(success);
    }

    // 如有需要，这里可添加课程修改、删除等接口
}