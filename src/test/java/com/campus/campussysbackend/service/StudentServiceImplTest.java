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

import java.lang.reflect.Field;
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
    void setUp() throws Exception {
        // 手动将 mock 的 studentMapper 注入到 ServiceImpl 的 baseMapper 字段，避免 MyBatis-Plus 抛 baseMapper can not be null
        Field baseMapperField = StudentServiceImpl.class.getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(studentService, studentMapper);
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
        // 为了触发内部逻辑并避免不必要 stubbing，这里让 selectById 返回一个存在的 Student，
        // 然后在 auditStudent 中因为找不到 SysUser 抛异常。
        Student student = new Student();
        student.setId(1);
        student.setLoginId(10);
        when(studentMapper.selectById(anyInt())).thenReturn(student);
        when(sysUserMapper.selectById(10)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> studentService.auditStudentsBatch(Arrays.asList(1, 2)));
    }
}
