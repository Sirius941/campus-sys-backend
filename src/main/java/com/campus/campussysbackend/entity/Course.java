package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String courseName;      // 课程名称
    private LocalDateTime createdTime; // 创建时间
    private String status;          // 课程状态：0已创建，1已开放，2已结束
    private Integer activityId;     // 关联活动ID
    private Integer teacherId;      // 授课教师ID
    private Integer classId;        // 关联教学班ID
}