package com.campus.campussysbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper; // 新增：直接注入 mapper，避免 baseMapper 为空导致 getOne 抛异常

    @Override
    public String login(String loginName, String password) {
        // 1. 根据登录名查询用户（直接用 mapper）
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginName, loginName);
        SysUser user = sysUserMapper.selectOne(wrapper);

        // 2. 校验用户是否存在
        if (user == null) {
            throw new RuntimeException("登录失败：用户不存在");
        }

        // 3. 校验密码
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("登录失败：密码错误");
        }

        // 4. 校验账号状态
        if ("2".equals(user.getStatus())) {
            throw new RuntimeException("登录失败：账号异常或已被删除");
        }

        // 5. 生成 Token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 6. 写入 Redis（如果可用）
        if (redisTemplate != null) {
            String redisKey = "login:token:" + token;
            redisTemplate.opsForValue().set(redisKey, user.getId().toString(), 30, TimeUnit.MINUTES);
        }

        return token;
    }
}