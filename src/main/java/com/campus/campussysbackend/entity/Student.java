package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_manager_student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String stuName;      // 学生姓名
    private String fromPlace;    // 来源地址
    private String parentPhone;  // 监护人电话
    private String currentGrade; // 当前年级
    private String isFirst;      // 是否首次参加：1新生，0老生
    private Integer loginId;     // 关联登录id
    private String comment;      // 备注
}