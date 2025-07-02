package com.crieff.framework.auth.filter;


import com.crieff.framework.auth.common.utils.JwtUtil;
import com.crieff.framework.auth.model.JwtContent;
import com.crieff.framework.exception.BasicErrorCode;
import com.crieff.framework.exception.BizException;
import com.crieff.framework.redis.RedisHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @description:
 * @author: aKuang
 * @time: 10/25/22 5:01 PM
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    public static String userTokenKey = "crieff:admin:user:";

    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisHelper redisHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            // 未登录
            filterChain.doFilter(request, response);
            return;
        }
        // token格式是否正确
        if (!jwtUtil.isJwtStr(token)) {
            throw new BizException(BasicErrorCode.AUTH_ERROR, "token不合法");
        }
        // token是否过期
        if (jwtUtil.isExpired(token)) {
            throw new BizException(BasicErrorCode.AUTH_ERROR, "token已经过期");
        }
        // 获取当前用户信息
        JwtContent content = jwtUtil.getTokenSubjectObject(token);
        String userTokenKey = String.join(":", JwtAuthenticationTokenFilter.userTokenKey, String.valueOf(content.getId()));
        String availableToken = (String) redisHelper.get(userTokenKey);
        if (StringUtils.isBlank(availableToken)) {
            throw new BizException(BasicErrorCode.AUTH_ERROR, "无此token认证信息");
        }
        token = token.replace("Bearer ", "");
        if (!StringUtils.equals(token, availableToken)) {
            throw new BizException(BasicErrorCode.AUTH_ERROR, "当前用户已在其他地方登陆");
        }
        //用三个构造参数得  告诉SpringSecurity 该用户已经认证成功
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(content, null, null);
        //存入SecurityContext 中  因为 SpringSecurity 后面需要内部验证
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
