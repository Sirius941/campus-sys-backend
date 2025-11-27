package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.entity.Teacher;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.mapper.TeacherMapper;
import com.campus.campussysbackend.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper; // 新增：直接注入 mapper，避免 baseMapper 为空导致 getOne 抛异常

    @Autowired
    private TeacherMapper teacherMapper; // 注入 TeacherMapper 用于判断是否为教师
    @Override
    public String login(String loginName, String password) {
        // 1. 根据登录名查询用户（直接用 mapper）
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginName, loginName);
        SysUser user = sysUserMapper.selectOne(wrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            throw new RuntimeException("登录失败：用户不存在");
        }

        // 3. 校验密码
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. 校验账号状态
        if ("2".equals(user.getStatus())) {
            throw new RuntimeException("登录失败：账号异常或已被删除");
        }

        // 5. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 6. 【关键修改】判断角色
        // 简单逻辑：
        // 1. 如果 loginName 是 "admin" -> 管理员
        // 2. 如果在 t_manager_teacher 表里能查到 loginId -> 教师
        // 3. 否则默认为 学生 (或根据 t_manager_student 查)

        String role = "student"; // 默认角色

        if ("admin".equals(loginName) || "root".equals(loginName)) {
            role = "admin";
        } else {
            // 查询是否是教师
            LambdaQueryWrapper<Teacher> teacherWrapper = new LambdaQueryWrapper<>();
            teacherWrapper.eq(Teacher::getLoginId, user.getId());
            if (teacherMapper.selectCount(teacherWrapper) > 0) {
                role = "teacher";
            }
        }

        // 7. 【关键修改】将 "userId:role" 格式存入 Redis
        // 例如: "10:admin" 或 "25:teacher"
        String redisValue = user.getId() + ":" + role;

        String redisKey = "login:token:" + token;
        redisTemplate.opsForValue().set(redisKey, redisValue, 30, TimeUnit.MINUTES);

        return token;
    }
}