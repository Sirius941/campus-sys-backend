package com.campus.campussysbackend.service;

import com.campus.campussysbackend.mapper.CampusActivityMapper;
import com.campus.campussysbackend.service.impl.CampusActivityServiceImpl;
import com.campus.campussysbackend.entity.CampusActivity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampusActivityServiceImplTest {

    @Mock
    private CampusActivityMapper campusActivityMapper;

    @InjectMocks
    private CampusActivityServiceImpl campusActivityService;

    @Test
    void publishActivity_success_shouldReturnTrue() {
        when(campusActivityMapper.updateById(any(CampusActivity.class))).thenReturn(1);
        boolean result = campusActivityService.publishActivity(10);
        assertTrue(result);
        verify(campusActivityMapper, times(1)).updateById(any(CampusActivity.class));
    }

    @Test
    void publishActivity_fail_shouldReturnFalse() {
        when(campusActivityMapper.updateById(any(CampusActivity.class))).thenReturn(0);
        boolean result = campusActivityService.publishActivity(11);
        assertFalse(result);
        verify(campusActivityMapper, times(1)).updateById(any(CampusActivity.class));
    }
}

