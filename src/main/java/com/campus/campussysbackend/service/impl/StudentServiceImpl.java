package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.Student;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.StudentMapper;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudentWithUser(Student student) {
        // 1. 创建关联的系统用户
        SysUser newUser = new SysUser();
        // 默认账号名：使用手机号 (parentPhone) 或 姓名+时间戳，防止重复
        newUser.setLoginName(student.getParentPhone());
        newUser.setPassword("123456"); // 默认密码

        // 关键点：设置为 "2" (异常/未激活) 或自定义 "0" (待审核)，等待管理员审核
        // 这里根据 SQL 注释 '1'正常 '2'异常，我们先设为 '2' 表示未审核通过
        newUser.setStatus("2");

        // 2. 保存用户以获取 ID
        sysUserMapper.insert(newUser);

        // 3. 关联 ID 并保存学员
        student.setLoginId(newUser.getId());
        this.save(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditStudent(Integer studentId) {
        // 1. 查询学员信息
        Student student = this.getById(studentId);
        if (student == null) {
            throw new RuntimeException("学员不存在");
        }

        // 2. 查询对应的用户
        SysUser user = sysUserMapper.selectById(student.getLoginId());
        if (user == null) {
            throw new RuntimeException("关联账号不存在");
        }

        // 3. 修改状态为 '1' (正常/已审核)
        user.setStatus("1");
        sysUserMapper.updateById(user);
    }

    /**
     * 实现批量审核
     * 简单的做法是循环调用单条审核逻辑，
     * 因为单条逻辑里已经处理了“查学员->找用户->改状态”的复杂过程。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditStudentsBatch(List<Integer> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return;
        }
        for (Integer id : studentIds) {
            // 复用已有的单条审核逻辑
            this.auditStudent(id);
        }
    }
}