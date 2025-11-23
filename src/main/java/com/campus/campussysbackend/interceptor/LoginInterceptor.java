package com.campus.campussysbackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 Token (约定 Header key 为 "Authorization" 或 "token")
        String token = request.getHeader("token");

        // 2. 如果 Token 为空，拦截
        if (!StringUtils.hasText(token)) {
            response.setStatus(401); // 401 未授权
            response.getWriter().write("Error: No Token Provided");
            return false;
        }

        // 3. 去 Redis 查询 Token 是否存在
        String redisKey = "login:token:" + token;
        String userId = redisTemplate.opsForValue().get(redisKey);

        if (userId == null) {
            // Token 不存在或已过期
            response.setStatus(401);
            response.getWriter().write("Error: Invalid or Expired Token");
            return false;
        }

        // 4. (可选) Token 续期：如果用户正在操作，重置过期时间为 30 分钟
        redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);

        // 5. 放行
        return true;
    }
}