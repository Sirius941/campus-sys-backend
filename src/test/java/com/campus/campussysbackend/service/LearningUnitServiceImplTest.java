package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.entity.LearningUnit;
import com.campus.campussysbackend.mapper.LearningUnitMapper;
import com.campus.campussysbackend.service.impl.LearningUnitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningUnitServiceImplTest {

    @Mock
    private LearningUnitMapper learningUnitMapper;

    @InjectMocks
    private LearningUnitServiceImpl learningUnitService;

    @Test
    void getUnitsByCourseId_success_shouldReturnUnits() {
        LearningUnit u1 = new LearningUnit();
        u1.setId(1);
        u1.setCourseId(100);
        LearningUnit u2 = new LearningUnit();
        u2.setId(2);
        u2.setCourseId(100);
        when(learningUnitMapper.selectList(any())).thenReturn(Arrays.asList(u1, u2));

        List<LearningUnit> result = learningUnitService.getUnitsByCourseId(100);
        assertEquals(2, result.size());
        verify(learningUnitMapper, times(1)).selectList(any());
    }

    @Test
    void getUnitsByCourseId_null_shouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> learningUnitService.getUnitsByCourseId(null));
        assertTrue(ex.getMessage().contains("courseId"));
        verifyNoInteractions(learningUnitMapper);
    }
}
