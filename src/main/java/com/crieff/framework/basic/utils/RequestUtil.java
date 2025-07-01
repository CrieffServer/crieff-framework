package com.crieff.framework.basic.utils;

import com.crieff.framework.auth.model.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @description:
 * @author: aKuang
 * @time: 10/26/22 5:09 PM
 */
public class RequestUtil {

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(attributes).map(ServletRequestAttributes::getRequest).orElse(null);
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUserName() {
        LoginUser userDetail = getCurrentUser();
        if (userDetail == null) {
            return "";
        }
        return userDetail.getUsername();
    }

    /**
     * 获取当前用户信息
     */
    public static LoginUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return (LoginUser) principal;
            }
        }
        return null;
    }
}
