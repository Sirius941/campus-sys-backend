package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_manager_class")
public class TeachingClass {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;            // 班级名称
    private LocalDateTime createdTime; // 创建时间
    private String classDesc;       // 描述
    private Integer teacherId;      // 负责教师ID
    private Integer activityId;     // 关联活动ID
}