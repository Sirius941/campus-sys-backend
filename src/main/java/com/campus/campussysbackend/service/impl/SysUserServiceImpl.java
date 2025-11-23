package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public String login(String loginName, String password) {
        // 1. 根据登录名查询用户
        // 文档要求：根据登录名匹配 [cite: 67]
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginName, loginName);
        SysUser user = this.getOne(wrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            throw new RuntimeException("登录失败：用户不存在");
        }

        /*
        // 3. 校验密码
        // 注意：实际项目中通常需加密（如MD5/BCrypt），期中项目如果未强制要求加密，可先明文比对
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("登录失败：密码错误");
        }

         */

        // 4. 校验账号状态
        // SQL定义：status '1'表示正常；'2'表示异常 [cite: 78]
        if ("2".equals(user.getStatus())) {
            throw new RuntimeException("登录失败：账号异常或已被删除");
        }

        // 5. 生成 Token
        // 文档要求：登录成功，获得请求Token [cite: 68]
        // 这里使用 UUID 生成一个简单的 Token
        String token = UUID.randomUUID().toString().replace("-", "");

        // TODO: 这里可以将 Token 存入 Redis 或 数据库，与 userId 绑定，以便后续拦截器校验

        // 5. 【新增】将 Token 存入 Redis
        // Key: "login:token:{token值}"  Value: 用户ID (或者存用户JSON信息)
        // 设置过期时间：30分钟
        String redisKey = "login:token:" + token;
        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), 30, TimeUnit.MINUTES);

        return token;
        return token;
    }
}