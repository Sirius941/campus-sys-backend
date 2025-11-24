package com.campus.campussysbackend.service;

import com.campus.campussysbackend.entity.CampusActivity;
import com.campus.campussysbackend.entity.TeachingClass;
import com.campus.campussysbackend.mapper.CampusActivityMapper;
import com.campus.campussysbackend.mapper.TeachingClassMapper;
import com.campus.campussysbackend.service.impl.TeachingClassServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeachingClassServiceImplTest {

    @Mock
    private CampusActivityMapper activityMapper;

    @Mock
    private TeachingClassMapper teachingClassMapper;

    @InjectMocks
    private TeachingClassServiceImpl teachingClassService;

    @Test
    void createClass_success_shouldInsert() {
        CampusActivity activity = new CampusActivity();
        activity.setId(88);
        activity.setStatus(1); // 已发布
        when(activityMapper.selectById(88)).thenReturn(activity);
        when(teachingClassMapper.insert(any(TeachingClass.class))).thenReturn(1);

        TeachingClass clazz = new TeachingClass();
        clazz.setActivityId(88);
        boolean ok = teachingClassService.createClass(clazz);
        assertTrue(ok);
        verify(teachingClassMapper, times(1)).insert(any(TeachingClass.class));
    }

    @Test
    void createClass_missingActivityId_shouldThrow() {
        TeachingClass clazz = new TeachingClass();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> teachingClassService.createClass(clazz));
        assertTrue(ex.getMessage().contains("必须关联一个活动"));
        verifyNoInteractions(activityMapper, teachingClassMapper);
    }

    @Test
    void createClass_activityNotFound_shouldThrow() {
        when(activityMapper.selectById(99)).thenReturn(null);
        TeachingClass clazz = new TeachingClass();
        clazz.setActivityId(99);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> teachingClassService.createClass(clazz));
        assertTrue(ex.getMessage().contains("活动不存在"));
        verify(activityMapper, times(1)).selectById(99);
        verifyNoInteractions(teachingClassMapper);
    }

    @Test
    void createClass_activityNotPublished_shouldThrow() {
        CampusActivity activity = new CampusActivity();
        activity.setId(77);
        activity.setStatus(0); // 未发布
        when(activityMapper.selectById(77)).thenReturn(activity);
        TeachingClass clazz = new TeachingClass();
        clazz.setActivityId(77);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> teachingClassService.createClass(clazz));
        assertTrue(ex.getMessage().contains("尚未发布"));
        verify(activityMapper, times(1)).selectById(77);
        verifyNoInteractions(teachingClassMapper);
    }
}

