package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.CampusActivity;
import com.campus.campussysbackend.mapper.CampusActivityMapper;
import com.campus.campussysbackend.service.ICampusActivityService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired; // 新增

@Service
public class CampusActivityServiceImpl extends ServiceImpl<CampusActivityMapper, CampusActivity> implements ICampusActivityService {

    @Autowired
    private CampusActivityMapper campusActivityMapper; // 新增：直接注入 mapper 便于 Mockito 测试

    @Override
    public boolean publishActivity(Integer id) {
        // 根据文档：根据活动id, 将活动的编辑状态修改为已发布 [cite: 43]
        CampusActivity activity = new CampusActivity();
        activity.setId(id);
        activity.setStatus(1); // 假设 1 代表已发布
        // 使用直接注入的 mapper，避免 baseMapper 为空造成 updateById 抛异常（单元测试环境下未注入）
        return campusActivityMapper.updateById(activity) == 1;
    }
}