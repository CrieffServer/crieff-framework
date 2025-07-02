package com.crieff.framework.auth.handler;

import com.crieff.framework.auth.common.utils.JwtUtil;
import com.crieff.framework.auth.filter.JwtAuthenticationTokenFilter;
import com.crieff.framework.auth.model.JwtContent;
import com.crieff.framework.auth.model.LoginUser;
import com.crieff.framework.redis.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import static com.crieff.framework.basic.constant.CrieffConstant.ONE_DAY_SECOND;

/**
 * @description:
 * @author: aKuang
 * @time: 2025/7/1 20:43
 */
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisHelper redisHelper;

    public void saveToken(LoginUser userDetail) {
        JwtContent tokenContent = new JwtContent();//自定义的实体类 用户信息 存储到jwt里
        BeanUtils.copyProperties(userDetail, tokenContent);
        String token = jwtUtil.generateToken(tokenContent);
        String userTokenKey = String.join(":", JwtAuthenticationTokenFilter.userTokenKey, String.valueOf(tokenContent.getId()));
        redisHelper.set(userTokenKey, token, ONE_DAY_SECOND);
    }
}
