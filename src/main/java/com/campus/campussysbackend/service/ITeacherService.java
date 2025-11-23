package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.Teacher;

public interface ITeacherService extends IService<Teacher> {

    /**
     * 新增教师，并自动创建对应的系统用户
     * 对应文档需求： 添加教师时，自动添加对应的用户记录
     * @param teacher 教师信息
     */
    void addTeacherWithUser(Teacher teacher);

}