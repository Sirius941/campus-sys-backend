package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.CampusActivity;
import com.campus.campussysbackend.entity.TeachingClass;
import com.campus.campussysbackend.mapper.CampusActivityMapper;
import com.campus.campussysbackend.mapper.TeachingClassMapper;
import com.campus.campussysbackend.service.ITeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TeachingClassServiceImpl extends ServiceImpl<TeachingClassMapper, TeachingClass> implements ITeachingClassService {

    @Autowired
    private CampusActivityMapper activityMapper;

    @Override
    public boolean createClass(TeachingClass teachingClass) {
        // 1. 校验必填项
        if (teachingClass.getActivityId() == null) {
            throw new RuntimeException("创建失败：必须关联一个活动");
        }

        // 2. 查询关联的活动
        CampusActivity activity = activityMapper.selectById(teachingClass.getActivityId());
        if (activity == null) {
            throw new RuntimeException("创建失败：关联的活动不存在");
        }

        // 3. 【核心约束】检查活动状态
        // 文档约束：只有当活动发布(status=1)后，相关的后续业务才能操作 [cite: 30]
        if (activity.getStatus() == null || activity.getStatus() != 1) {
            throw new RuntimeException("创建失败：关联的活动尚未发布，无法创建班级");
        }

        // 4. 补全信息
        teachingClass.setCreatedTime(LocalDateTime.now());

        // 5. 保存
        return this.save(teachingClass);
    }
}