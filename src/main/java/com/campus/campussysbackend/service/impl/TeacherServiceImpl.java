package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.entity.Teacher;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.mapper.TeacherMapper;
import com.campus.campussysbackend.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Autowired
    private SysUserMapper sysUserMapper; // 需要操作用户表

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务：如果任何一步失败，全部回滚
    public void addTeacherWithUser(Teacher teacher) {
        // 1. 准备用户数据 (SysUser)
        // 文档未规定默认账号密码规则，这里通常约定：
        // 账号 = 教师名字 或 手机号(如果有)
        // 密码 = 默认 "123456"
        SysUser newUser = new SysUser();
        newUser.setLoginName("T_" + System.currentTimeMillis()); // 示例：生成唯一登录名，实际可用 teacher.getTeacherName()
        newUser.setPassword("123456"); // 默认密码
        newUser.setStatus("1"); // 1表示正常 [cite: 78]
        newUser.setGender("M"); // 默认为M，或者前端传

        // 2. 插入用户 (MyBatis-Plus 插入后会自动将 ID 回填到 newUser 对象中)
        sysUserMapper.insert(newUser);

        // 3. 关联教师与用户
        // 将刚刚生成的 User ID 赋给 Teacher 的 login_id 字段 [cite: 78]
        teacher.setLoginId(newUser.getId());

        // 4. 插入教师
        this.save(teacher);
    }
}