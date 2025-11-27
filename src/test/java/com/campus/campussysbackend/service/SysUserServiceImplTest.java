package com.campus.campussysbackend.service;

import com.campus.campussysbackend.entity.SysUser;
import com.campus.campussysbackend.mapper.SysUserMapper;
import com.campus.campussysbackend.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserServiceImplTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    @Test
    void login_success_shouldReturnTokenAndStoreRedis() {
        SysUser user = new SysUser();
        user.setId(100);
        user.setLoginName("admin");
        user.setPassword("123456");
        user.setStatus("1");

        when(sysUserMapper.selectOne(any())).thenReturn(user); // 使用通配 any() 避免泛型警告
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String token = sysUserService.login("admin", "123456");

        assertNotNull(token);
        assertEquals(32, token.length());
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        // 期望存入的值为 "id:loginName"
        verify(valueOperations, times(1)).set(keyCaptor.capture(), eq("100:admin"), eq(30L), eq(java.util.concurrent.TimeUnit.MINUTES));
        assertTrue(keyCaptor.getValue().startsWith("login:token:"));
    }

    @Test
    void login_userNotFound_shouldThrow() {
        when(sysUserMapper.selectOne(any())).thenReturn(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sysUserService.login("x", "y"));
        assertTrue(ex.getMessage().contains("用户不存在"));
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void login_passwordError_shouldThrow() {
        SysUser user = new SysUser();
        user.setId(1);
        user.setLoginName("u");
        user.setPassword("right");
        user.setStatus("1");
        when(sysUserMapper.selectOne(any())).thenReturn(user);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sysUserService.login("u", "wrong"));
        assertTrue(ex.getMessage().contains("密码错误"));
    }

    @Test
    void login_statusAbnormal_shouldThrow() {
        SysUser user = new SysUser();
        user.setId(1);
        user.setLoginName("u");
        user.setPassword("123");
        user.setStatus("2");
        when(sysUserMapper.selectOne(any())).thenReturn(user);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> sysUserService.login("u", "123"));
        assertTrue(ex.getMessage().contains("账号异常"));
    }
}
