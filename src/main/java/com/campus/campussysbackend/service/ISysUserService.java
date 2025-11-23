package com.campus.campussysbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.campussysbackend.entity.SysUser;

public interface ISysUserService extends IService<SysUser> {

    /**
     * 用户登录
     * @param loginName 登录名
     * @param password 密码
     * @return 登录成功生成的 Token
     */
    String login(String loginName, String password);

}