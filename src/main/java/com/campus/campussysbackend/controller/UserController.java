package com.campus.campussysbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.campussysbackend.common.Result;
import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * a) 用户登录 [cite: 67]
     * 需求：登录成功，获得请求Token [cite: 68]
     */
    @Operation(summary = "用户登录，成功返回Token")
    @PostMapping("/login")
    // 这里使用 Map 接收参数，也可以直接用 SysUser 对象接收
    public Result<String> login(@RequestBody Map<String, String> loginParams) {
        String loginName = loginParams.get("loginName");
        String password = loginParams.get("password");

        if (!StringUtils.hasText(loginName) || !StringUtils.hasText(password)) {
            return Result.error("账号或密码不能为空");
        }

        try {
            // 调用 Service 层已实现的登录逻辑（含密码校验和状态校验）
            String token = sysUserService.login(loginName, password);
            return Result.success(token);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * b) 新增用户
     * 注意：这里通常指新增管理员或其他非关联角色。
     * 教师和学员通常通过各自的业务接口（addTeacher/addStudent）自动新增用户。
     */
    @Operation(summary = "新增用户")
    @PostMapping("/add")
    public Result<Boolean> addUser(@RequestBody SysUser user) {
        // 1. 简单校验
        if (!StringUtils.hasText(user.getLoginName()) || !StringUtils.hasText(user.getPassword())) {
            return Result.error("账号和密码必填");
        }

        // 2. 设置默认值
        // 根据 SQL，status '1' 表示正常
        if (user.getStatus() == null) {
            user.setStatus("1");
        }

        // 3. 校验账号是否已存在 (防止重复注册)
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginName, user.getLoginName());
        if (sysUserService.count(wrapper) > 0) {
            return Result.error("该登录名已存在");
        }

        // 4. 保存
        boolean success = sysUserService.save(user);
        return Result.success(success);
    }

    /**
     * c) 用户查询：按照名字或id查询
     */
    @Operation(summary = "用户查询，支持按名字（模糊）或ID（精确）查询")
    @GetMapping("/list")
    public Result<List<SysUser>> listUsers(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Integer id) {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        // 按 ID 精确查询
        if (id != null) {
            wrapper.eq(SysUser::getId, id);
        }

        // 按名字 (login_name) 模糊查询
        // 注意：t_sys_user 表只有 login_name，没有 real_name (那是教师/学员表的字段)
        if (StringUtils.hasText(name)) {
            wrapper.like(SysUser::getLoginName, name);
        }

        // 按 ID 排序
        wrapper.orderByDesc(SysUser::getId);

        return Result.success(sysUserService.list(wrapper));
    }

    /**
     * 补充：获取当前用户详情 (常用于前端回显)
     */
    @GetMapping("/{id}")
    public Result<SysUser> getUserById(@PathVariable Integer id) {
        return Result.success(sysUserService.getById(id));
    }
}