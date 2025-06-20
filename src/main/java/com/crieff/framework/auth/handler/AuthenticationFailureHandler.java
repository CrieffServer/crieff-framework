package com.crieff.framework.auth.handler;

import com.crieff.framework.basic.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户token认证失败处理
 * @description:
 * @author: aKuang
 * @time: 10/12/22 2:48 PM
 */
@Component
public class AuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpStatus.OK.value());
        ObjectMapper om = new ObjectMapper();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.getOutputStream()
                .println(om.writeValueAsString(Response.fail(HttpStatus.UNAUTHORIZED.value(), "user authenticate fail!")));

    }
}
