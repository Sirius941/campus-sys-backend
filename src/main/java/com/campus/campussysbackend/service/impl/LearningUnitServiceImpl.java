package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.LearningUnit;
import com.campus.campussysbackend.mapper.LearningUnitMapper;
import com.campus.campussysbackend.service.ILearningUnitService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class LearningUnitServiceImpl extends ServiceImpl<LearningUnitMapper, LearningUnit> implements ILearningUnitService {

    @Autowired
    private LearningUnitMapper learningUnitMapper;

    @Override
    public List<LearningUnit> getUnitsByCourseId(Integer courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("courseId 不能为空");
        }
        LambdaQueryWrapper<LearningUnit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningUnit::getCourseId, courseId);

        // 排序逻辑：建议先按父ID排序（把章排一起），再按ID排序
        // 实际项目中可能需要更复杂的树形结构组装，这里先返回列表
        wrapper.orderByAsc(LearningUnit::getId);

        // 使用直接注入的 mapper，避免在单元测试中 baseMapper 为空导致 this.list(wrapper) 抛异常
        return learningUnitMapper.selectList(wrapper);
    }
}