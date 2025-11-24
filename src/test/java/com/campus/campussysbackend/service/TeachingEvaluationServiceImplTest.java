package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.entity.TeachingEvaluation;
import com.campus.campussysbackend.mapper.TeachingEvaluationMapper;
import com.campus.campussysbackend.service.impl.TeachingEvaluationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeachingEvaluationServiceImplTest {

    @Mock
    private TeachingEvaluationMapper evaluationMapper;

    @InjectMocks
    private TeachingEvaluationServiceImpl evaluationService;

    @Test
    void addEvaluation_success_shouldInsert() {
        TeachingEvaluation ev = new TeachingEvaluation();
        ev.setStuInClassId(1);
        ev.setCourseId(2);
        ev.setEvaluator(3);

        when(evaluationMapper.insert(any(TeachingEvaluation.class))).thenReturn(1);

        boolean ok = evaluationService.addEvaluation(ev);
        assertTrue(ok);
        assertNotNull(ev.getEvaulatedTime());
        verify(evaluationMapper, times(1)).insert(any(TeachingEvaluation.class));
    }

    @Test
    void addEvaluation_missingFields_shouldThrow() {
        TeachingEvaluation ev = new TeachingEvaluation();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> evaluationService.addEvaluation(ev));
        assertTrue(ex.getMessage().contains("评价失败"));
        verifyNoInteractions(evaluationMapper);
    }

    @Test
    void getEvaluationsByCourseId_success_shouldReturnList() {
        TeachingEvaluation e1 = new TeachingEvaluation();
        e1.setId(10); e1.setCourseId(55); e1.setEvaulatedTime(LocalDateTime.now());
        TeachingEvaluation e2 = new TeachingEvaluation();
        e2.setId(11); e2.setCourseId(55); e2.setEvaulatedTime(LocalDateTime.now().minusMinutes(1));
        when(evaluationMapper.selectList(any())).thenReturn(Arrays.asList(e1, e2));
        List<TeachingEvaluation> list = evaluationService.getEvaluationsByCourseId(55);
        assertEquals(2, list.size());
        verify(evaluationMapper, times(1)).selectList(any());
    }
}
