package com.campus.campussysbackend.service;

import com.campus.campussysbackend.entity.TeachingStudentInClass;
import com.campus.campussysbackend.mapper.TeachingStudentInClassMapper;
import com.campus.campussysbackend.service.impl.TeachingStudentInClassServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeachingStudentInClassServiceImplTest {

    @Mock
    private TeachingStudentInClassMapper teachingStudentInClassMapper;

    @InjectMocks
    private TeachingStudentInClassServiceImpl studentInClassService;

    @Test
    void addStudentsToClass_success_shouldInsertNewOnly() {
        // 对第一个学生：count=0 -> 插入；第二个学生：count>0 -> 跳过
        when(teachingStudentInClassMapper.selectCount(any()))
                .thenReturn(0L)  // for first student
                .thenReturn(1L); // for second student (already exists)
        when(teachingStudentInClassMapper.insert(any(TeachingStudentInClass.class))).thenReturn(1);

        studentInClassService.addStudentsToClass(100, Arrays.asList(201, 202));

        // 期望：selectCount 调用两次，insert 只调用一次
        verify(teachingStudentInClassMapper, times(2)).selectCount(any());
        verify(teachingStudentInClassMapper, times(1)).insert(any(TeachingStudentInClass.class));
    }

    @Test
    void addStudentsToClass_emptyList_shouldThrow() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> studentInClassService.addStudentsToClass(100, null));
        assertTrue(ex.getMessage().contains("不能为空"));
        verifyNoInteractions(teachingStudentInClassMapper);
    }
}
