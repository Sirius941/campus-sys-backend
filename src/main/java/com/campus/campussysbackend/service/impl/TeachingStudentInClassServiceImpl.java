package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.TeachingStudentInClass;
import com.campus.campussysbackend.mapper.TeachingStudentInClassMapper;
import com.campus.campussysbackend.service.ITeachingStudentInClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired; // 新增
import java.util.List;

@Service
public class TeachingStudentInClassServiceImpl extends ServiceImpl<TeachingStudentInClassMapper, TeachingStudentInClass> implements ITeachingStudentInClassService {

    @Autowired
    private TeachingStudentInClassMapper teachingStudentInClassMapper; // 新增：便于测试

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudentsToClass(Integer classId, List<Integer> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            throw new RuntimeException("学员列表不能为空");
        }

        for (Integer studentId : studentIds) {
            // 1. 查重：检查该学员是否已经在这个班级里
            LambdaQueryWrapper<TeachingStudentInClass> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TeachingStudentInClass::getClassId, classId);
            wrapper.eq(TeachingStudentInClass::getStudentId, studentId);

            Long count = teachingStudentInClassMapper.selectCount(wrapper);
            if (count != null && count > 0) {
                // 如果已存在，可以选择跳过或报错。这里选择跳过，继续处理下一个。
                continue;
            }

            // 2. 插入关联记录
            TeachingStudentInClass relation = new TeachingStudentInClass();
            relation.setClassId(classId);
            relation.setStudentId(studentId);
            // groupId 默认为空，由教辅后续分配

            teachingStudentInClassMapper.insert(relation);
        }
    }
}