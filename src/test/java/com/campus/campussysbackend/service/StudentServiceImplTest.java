package com.campus.campussysbackend.service;

import com.campus.campussysbackend.entity.Student;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.StudentMapper;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private SysUserMapper sysUserMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        // 不再通过反射设置 baseMapper，StudentServiceImpl 已直接注入 StudentMapper
        // @InjectMocks 会把上面的 @Mock 注入到 studentService 对应字段
    }

    @Test
    void addStudentWithUser_shouldCreateUserAndStudent() {
        Student student = new Student();
        student.setParentPhone("13800000000");

        // 模拟插入用户后主键回填
        doAnswer(invocation -> {
            SysUser arg = invocation.getArgument(0);
            arg.setId(1); // 模拟数据库生成的ID
            return 1;
        }).when(sysUserMapper).insert(any(SysUser.class));

        // 执行
        studentService.addStudentWithUser(student);

        // 验证 SysUser 被正确插入
        verify(sysUserMapper, times(1)).insert(any(SysUser.class));
        // 验证 Student 保存，loginId 已被设置
        verify(studentMapper, times(1)).insert(any(Student.class));
        assertEquals(1, student.getLoginId());
    }

    @Test
    void auditStudent_shouldUpdateUserStatusToNormal() {
        Integer studentId = 1;
        Student student = new Student();
        student.setId(studentId);
        student.setLoginId(10);

        SysUser user = new SysUser();
        user.setId(10);
        user.setStatus("2");

        when(studentMapper.selectById(eq(studentId))).thenReturn(student);
        when(sysUserMapper.selectById(eq(10))).thenReturn(user);

        studentService.auditStudent(studentId);

        assertEquals("1", user.getStatus());
        verify(sysUserMapper, times(1)).updateById(user);
    }

    @Test
    void auditStudentsBatch_shouldHandleEmptyList() {
        studentService.auditStudentsBatch(Collections.emptyList());
        // 不应有任何数据库调用
        verifyNoInteractions(studentMapper, sysUserMapper);
    }

    @Test
    void auditStudentsBatch_shouldLoopOverIds() {
        // 让 selectById 根据不同 id 返回不同结果，避免不必要的 stubbing
        Student s1 = new Student();
        s1.setId(1);
        s1.setLoginId(10);
        Student s2 = new Student();
        s2.setId(2);
        s2.setLoginId(20);
        when(studentMapper.selectById(1)).thenReturn(s1);
        when(studentMapper.selectById(2)).thenReturn(s2);

        SysUser u1 = new SysUser();
        u1.setId(10);
        u1.setStatus("2");
        SysUser u2 = new SysUser();
        u2.setId(20);
        u2.setStatus("2");
        when(sysUserMapper.selectById(10)).thenReturn(u1);
        when(sysUserMapper.selectById(20)).thenReturn(u2);

        studentService.auditStudentsBatch(Arrays.asList(1, 2));

        // 两个用户都应被更新为正常状态
        assertEquals("1", u1.getStatus());
        assertEquals("1", u2.getStatus());
        verify(sysUserMapper, times(1)).updateById(u1);
        verify(sysUserMapper, times(1)).updateById(u2);
    }
}
