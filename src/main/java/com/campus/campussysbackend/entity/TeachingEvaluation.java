package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_teaching_evaluation")
public class TeachingEvaluation {
    @TableId(type = IdType.AUTO) // 建议在数据库将此字段设为自增
    private Integer id;

    private Integer stuInClassId;   // 关联 t_teaching_student_in_class 表的主键
    private String grade;           // 评价等级：0优秀 1良好 2中等 3较差
    private String evaluation;      // 评价内容
    private LocalDateTime evaulatedTime; // 评价时间 (注意拼写：SQL中是 evaulated_time)
    private Integer courseId;       // 关联课程ID
    private Integer evaluator;      // 评价教师ID
}