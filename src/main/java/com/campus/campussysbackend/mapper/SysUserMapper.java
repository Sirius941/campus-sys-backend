package com.campus.campussysbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.campussysbackend.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 这里可以为空，BaseMapper 已经内置了 CRUD 方法
}