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

    @Autowired
    private StudentMapper studentMapper; // 新增：直接注入自身 mapper，测试中可 mock

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudentWithUser(Student student) {
        SysUser newUser = new SysUser();
        newUser.setLoginName(student.getParentPhone());
        newUser.setPassword("123456");
        newUser.setStatus("2");
        sysUserMapper.insert(newUser);

        student.setLoginId(newUser.getId());
        studentMapper.insert(student); // 使用注入的 mapper
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditStudent(Integer studentId) {
        Student student = studentMapper.selectById(studentId); // 使用注入的 mapper
        if (student == null) {
            throw new RuntimeException("学员不存在");
        }
        SysUser user = sysUserMapper.selectById(student.getLoginId());
        if (user == null) {
            throw new RuntimeException("关联账号不存在");
        }
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
            this.auditStudent(id);
        }
    }
}