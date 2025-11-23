package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_teaching_student_in_class")
public class TeachingStudentInClass {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer classId;    // 班级ID
    private Integer studentId;  // 学生ID
    private Integer groupId;    // 小组ID (入班时通常为空，后续分组再填)
}