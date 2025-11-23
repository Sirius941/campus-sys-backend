package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_manager_teacher")
public class Teacher {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String teacherName;
    private String subject;
    private String profession;
    private String school;
    private String introduce;
    private Integer loginId; // 关联 t_sys_user 的 id
}
