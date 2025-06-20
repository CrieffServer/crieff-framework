package com.crieff.framework.auth.filter;


import com.alibaba.fastjson2.JSON;
import com.crieff.framework.auth.model.JwtContent;
import com.crieff.framework.auth.common.utils.JwtUtil;
import com.crieff.framework.auth.model.LoginUser;
import com.crieff.framework.exception.BasicErrorCode;
import com.crieff.framework.exception.BizException;
import com.crieff.framework.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: aKuang
 * @time: 10/25/22 5:01 PM
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    public static String userTokenKey = "crieff:system:user:";

    @Value("${token.head}")
    String authorization;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            // 未登录
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        boolean flag = jwtUtil.isJwtStr(token);
        if (!flag) {
            throw new BizException(BasicErrorCode.PARAM_INVALID, "token不合法");
        }
        flag = jwtUtil.isExpired(token);
        if (flag) {
            throw new BizException(BasicErrorCode.SERVER_ERROR, "token已经过期");
        }
        JwtContent content = jwtUtil.getTokenSubjectObject(token);
        //redis获取用户
        String jsonData = (String) redisUtil.get(String.join(":", userTokenKey, String.valueOf(content.getUserId())));
        LoginUser userInfo = JSON.parseObject(jsonData, LoginUser.class);

        if (userInfo == null) {
            throw new BizException(BasicErrorCode.SERVER_ERROR, "用户未登录");
        }

        //用三个构造参数得  告诉SpringSecurity 该用户已经认证成功
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, null);
        //存入SecurityContext 中  因为 SpringSecurity 后面需要内部验证
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
