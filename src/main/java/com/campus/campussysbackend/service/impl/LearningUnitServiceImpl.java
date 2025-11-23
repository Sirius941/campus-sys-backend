package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.LearningUnit;
import com.campus.campussysbackend.mapper.LearningUnitMapper;
import com.campus.campussysbackend.service.ILearningUnitService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LearningUnitServiceImpl extends ServiceImpl<LearningUnitMapper, LearningUnit> implements ILearningUnitService {

    @Override
    public List<LearningUnit> getUnitsByCourseId(Integer courseId) {
        LambdaQueryWrapper<LearningUnit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LearningUnit::getCourseId, courseId);

        // 排序逻辑：建议先按父ID排序（把章排一起），再按ID排序
        // 实际项目中可能需要更复杂的树形结构组装，这里先返回列表
        wrapper.orderByAsc(LearningUnit::getId);

        return this.list(wrapper);
    }
}