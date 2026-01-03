package com.sky.utils;

import com.sky.context.BaseContext;
import com.sky.constant.JwtClaimsConstant;
import com.sky.exception.UserNotLoginException;
import com.sky.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

/**
 * 当前用户工具类（修正版）
 */
public class CurrentUserUtil {

    /**
     * 获取当前登录用户ID（从ThreadLocal中获取，由拦截器提前解析）
     */
    public static Long getCurrentUserId() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new UserNotLoginException("用户未登录");
        }
        return userId;
    }

    /**
     * 从JWT令牌解析用户ID（备用方法）
     */
    public static Long parseUserIdFromToken(String token, JwtProperties jwtProperties) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            return Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
        } catch (JwtException e) {
            throw new UserNotLoginException("令牌解析失败");
        }
    }
}