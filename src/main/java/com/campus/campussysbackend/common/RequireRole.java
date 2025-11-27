package com.campus.campussysbackend.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE}) // 可用在方法或类上
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    String value(); // 角色名称，如 "admin", "teacher"
}