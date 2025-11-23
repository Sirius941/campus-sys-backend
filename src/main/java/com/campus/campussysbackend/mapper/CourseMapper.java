package com.campus.campussysbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.campussysbackend.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {}