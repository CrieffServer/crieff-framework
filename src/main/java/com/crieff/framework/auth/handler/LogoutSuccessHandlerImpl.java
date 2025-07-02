package com.crieff.framework.auth.handler;

import com.crieff.framework.auth.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: aKuang
 * @time: 2025/7/1 16:04
 */
@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser userDetail = (LoginUser) authentication.getPrincipal();
        log.info("{} logout success", userDetail.getAccount());
    }
}
