package com.campus.campussysbackend.interceptor;

import com.campus.campussysbackend.common.RequireRole; // 1. 引入自定义注解
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod; // 2. 引入 HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 【新增】 0. 如果不是映射到 Controller 方法的请求（如静态资源），直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 1. 获取请求头中的 Token
        String token = request.getHeader("token");

        // 2. 如果 Token 为空，拦截
        if (!StringUtils.hasText(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"msg\": \"Error: No Token Provided\"}");
            return false;
        }

        // 3. 去 Redis 查询 Token 是否存在
        String redisKey = "login:token:" + token;
        // 【关键修改】这里取出的 value 应该是 "userId:role" 格式（如 "1:admin"）
        String redisValue = redisTemplate.opsForValue().get(redisKey);

        if (redisValue == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\": 401, \"msg\": \"Error: Invalid or Expired Token\"}");
            return false;
        }

        // 4. Token 续期
        redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);

        // ================== 鉴权逻辑开始 ==================

        // 5. 解析 Redis 中的用户角色
        // 假设 Redis 存的值格式为 "userId:role"
        String[] parts = redisValue.split(":");
        // String userId = parts[0]; // 如果需要 userId 可以这样获取
        // 如果 split 后长度大于1，取第二部分作为角色，否则默认为 student
        String currentUserRole = parts.length > 1 ? parts[1] : "student";

        // 6. 获取注解鉴权信息
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 优先获取方法上的注解
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        // 如果方法上没有，再尝试获取类上的注解
        if (requireRole == null) {
            requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
        }

        // 7. 如果存在权限注解，进行对比
        if (requireRole != null) {
            String requiredRole = requireRole.value();

            // 简单鉴权逻辑：
            // 1. 如果当前用户是 admin，通常拥有所有权限（直接放行）
            // 2. 或者当前用户角色与注解要求的角色一致
            if (!"admin".equals(currentUserRole) && !requiredRole.equals(currentUserRole)) {
                // 权限不足
                response.setStatus(403); // 403 Forbidden
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\": 403, \"msg\": \"无权访问：角色不匹配\"}");
                return false;
            }
        }

        // ================== 鉴权逻辑结束 ==================

        // 8. 放行
        return true;
    }
}