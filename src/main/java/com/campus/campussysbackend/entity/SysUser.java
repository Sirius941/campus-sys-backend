package com.campus.campussysbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_sys_user") // 对应数据库表名
public class SysUser {
    @TableId(type = IdType.AUTO) // 主键自增
    private Integer id;

    private String loginName;
    private String password;
    private String gender; // M/F
    private LocalDateTime birthday;
    private String phone;
    private String email;
    private String status; // 1正常 2异常
}