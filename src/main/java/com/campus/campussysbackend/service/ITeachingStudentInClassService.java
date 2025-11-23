package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.TeachingStudentInClass;
import java.util.List;

public interface ITeachingStudentInClassService extends IService<TeachingStudentInClass> {
    /**
     * 将一批学员添加到指定班级
     * @param classId 班级ID
     * @param studentIds 学员ID列表
     */
    void addStudentsToClass(Integer classId, List<Integer> studentIds);
}