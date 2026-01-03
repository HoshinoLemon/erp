package com.sky.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * BCrypt密码加密工具类
 */
@Slf4j
public class BCryptPasswordEncoder {

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encode(String rawPassword) {
        // 生成盐值并加密（BCrypt会自动生成随机盐值）
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 校验密码
     * @param rawPassword 原始密码（用户输入的密码）
     * @param encodedPassword 加密后的密码（数据库存储的密码）
     * @return 校验结果（true: 密码匹配；false: 密码不匹配）
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }
        log.info("rawPassword:{},encodedPassword:{},encodedRawPassword:{}\n", rawPassword, encodedPassword, encode(rawPassword));
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}