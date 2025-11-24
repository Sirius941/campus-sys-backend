package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.Course;
import com.campus.campussysbackend.mapper.CourseMapper;
import com.campus.campussysbackend.service.ICourseService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; // 新增

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private CourseMapper courseMapper; // 新增：便于测试

    // 新增一个简单业务方法：开放课程（修改状态为 '1'）
    public boolean openCourse(Integer id) {
        Course c = new Course();
        c.setId(id);
        c.setStatus("1");
        return courseMapper.updateById(c) == 1;
    }
}