package com.campus.campussysbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.campussysbackend.entity.TeachingClass;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachingClassMapper extends BaseMapper<TeachingClass> {
    // MyBatis-Plus 会自动实现基础 CRUD，无需手写
}