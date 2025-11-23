package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.CampusActivity;
import com.campus.campussysbackend.mapper.CampusActivityMapper;
import com.campus.campussysbackend.service.ICampusActivityService;
import org.springframework.stereotype.Service;

@Service
public class CampusActivityServiceImpl extends ServiceImpl<CampusActivityMapper, CampusActivity> implements ICampusActivityService {

    @Override
    public boolean publishActivity(Integer id) {
        // 根据文档：根据活动id, 将活动的编辑状态修改为已发布 [cite: 43]
        CampusActivity activity = new CampusActivity();
        activity.setId(id);
        activity.setStatus(1); // 假设 1 代表已发布
        return this.updateById(activity);
    }
}