package com.campus.campussysbackend.service;

import com.campus.campussysbackend.mapper.CourseMapper;
import com.campus.campussysbackend.service.impl.CourseServiceImpl;
import com.campus.campussysbackend.entity.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void openCourse_success_shouldReturnTrue() {
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);
        boolean result = courseService.openCourse(5);
        assertTrue(result);
        verify(courseMapper, times(1)).updateById(any(Course.class));
    }

    @Test
    void openCourse_fail_shouldReturnFalse() {
        when(courseMapper.updateById(any(Course.class))).thenReturn(0);
        boolean result = courseService.openCourse(6);
        assertFalse(result);
        verify(courseMapper, times(1)).updateById(any(Course.class));
    }
}

