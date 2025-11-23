package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_course_learn_unit")
public class LearningUnit {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String unitTitle;     // 单元名称
    private String docPath;       // 文档地址
    private String videoPath;     // 视频地址
    private String unitIntroduce; // 介绍
    private Integer fatherId;     // 父单元ID (章ID)
    private String isChpt;        // 是否为章：1是，0否(小节)
    private Integer courseId;     // 关联课程ID
    private Integer learningScore;// 学习积分
}