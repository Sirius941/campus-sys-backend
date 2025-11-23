package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.CampusActivity;
import com.campus.campussysbackend.service.ICampusActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils; // 引入工具类
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ICampusActivityService activityService;

    // 1. 活动创建
    @PostMapping("/create")
    public Result<Boolean> createActivity(@RequestBody CampusActivity activity) {
        // 【关键修正】补全数据库必填字段 created_time
        activity.setCreatedTime(LocalDateTime.now());

        // 设置初始状态为0（已创建）
        activity.setStatus(0);

        // 【建议】设置创建者 (如果暂时没有拦截器获取当前用户，先写死或留空)
        if (activity.getCreator() == null) {
            activity.setCreator("admin"); // 暂时写死，后续结合 Token 获取
        }

        boolean saved = activityService.save(activity);
        return Result.success(saved);
    }

    // 2. 活动修改 [cite: 41]
    @PutMapping("/update")
    public Result<Boolean> updateActivity(@RequestBody CampusActivity activity) {
        // updateById 只会更新非空字段，非常适合这里
        return Result.success(activityService.updateById(activity));
    }

    // 3. 活动发布
    @PutMapping("/publish/{id}")
    public Result<Boolean> publishActivity(@PathVariable Integer id) {
        return Result.success(activityService.publishActivity(id));
    }

    // 4. 活动查询
    @GetMapping("/list")
    public Result<List<CampusActivity>> listActivities(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<CampusActivity> wrapper = new LambdaQueryWrapper<>();

        // 模糊查询：使用 StringUtils.hasText 防止传入空字符串 ""
        if (StringUtils.hasText(name)) {
            wrapper.like(CampusActivity::getName, name);
        }

        // 状态查询
        if (status != null) {
            wrapper.eq(CampusActivity::getStatus, status);
        }

        // 排序：按照创建时间降序
        wrapper.orderByDesc(CampusActivity::getCreatedTime);

        return Result.success(activityService.list(wrapper));
    }
}