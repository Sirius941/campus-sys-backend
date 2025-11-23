package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.Student;
import java.util.List;

public interface IStudentService extends IService<Student> {
    /**
     * 新增学员（包含自动创建账号逻辑）
     */
    void addStudentWithUser(Student student);

    /**
     * 审核学员（激活账号）
     */
    void auditStudent(Integer studentId);
    /**
     * 【新增】批量审核学员
     * @param studentIds 学员ID列表
     */
    void auditStudentsBatch(List<Integer> studentIds);
}