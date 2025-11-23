package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired; // 1. 新增引用
import org.springframework.data.redis.core.StringRedisTemplate; // 2. 新增引用
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit; // 3. 新增引用

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired // 4. 注入 Redis 工具类
    private StringRedisTemplate redisTemplate;

    @Override
    public String login(String loginName, String password) {
        // 1. 根据登录名查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginName, loginName);
        SysUser user = this.getOne(wrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            throw new RuntimeException("登录失败：用户不存在");
        }

        // 3. 校验密码 (实际项目中建议加密)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. 校验账号状态
        // SQL定义：status '1'表示正常；'2'表示异常
        if ("2".equals(user.getStatus())) {
            throw new RuntimeException("登录失败：账号异常或已被删除");
        }

        // 5. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 6. 【新增】将 Token 存入 Redis
        // Key: "login:token:{token值}"  Value: 用户ID
        // 设置过期时间：30分钟
        String redisKey = "login:token:" + token;
        // 这里现在可以正常调用 redisTemplate 和 TimeUnit 了
        redisTemplate.opsForValue().set(redisKey, user.getId().toString(), 30, TimeUnit.MINUTES);

        return token;
    }
}