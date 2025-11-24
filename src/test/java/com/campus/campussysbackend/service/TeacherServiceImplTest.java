package com.campus.campussysbackend.service;

import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.entity.Teacher;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.mapper.TeacherMapper;
import com.campus.campussysbackend.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void addTeacherWithUser_success_shouldInsertUserAndTeacher() {
        // 模拟用户插入后主键回填
        doAnswer(invocation -> {
            SysUser u = invocation.getArgument(0);
            u.setId(500); // 模拟生成ID
            return 1;
        }).when(sysUserMapper).insert(any(SysUser.class));

        Teacher teacher = new Teacher();
        teacher.setTeacherName("Alice");

        teacherService.addTeacherWithUser(teacher);

        // 验证用户插入
        verify(sysUserMapper, times(1)).insert(any(SysUser.class));
        // 验证教师插入且 loginId 关联
        verify(teacherMapper, times(1)).insert(any(Teacher.class));
        assertEquals(500, teacher.getLoginId());
    }
}

