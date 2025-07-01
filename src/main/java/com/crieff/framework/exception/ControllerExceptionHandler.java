package com.crieff.framework.exception;

import com.crieff.framework.basic.model.Response;
import com.crieff.framework.basic.utils.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Api接口统一异常处理
 *
 * @description:
 * @author: aKuang
 * @time: 10/26/22 5:04 PM
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Response handler(Exception e) {
        HttpServletRequest httpServletRequest = RequestUtil.getHttpServletRequest();
        if (ObjectUtils.isEmpty(httpServletRequest)) {
            log.error("系统异常，error", e);
        }
        log.error("系统异常，请求路径:[{}]，error", httpServletRequest.getRequestURI(), e);
        return Response.fail(e);
    }

    @ExceptionHandler(value = BizException.class)
    public Response handler(BizException e) {
        HttpServletRequest httpServletRequest = RequestUtil.getHttpServletRequest();
        if (ObjectUtils.isEmpty(httpServletRequest)) {
            log.error("业务异常，error", e);
        }
        log.error("业务异常，请求路径:[{}]，原因:{}", httpServletRequest.getRequestURI(), e.getClass().getSimpleName());
        return Response.fail(e);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public Response handler(AuthenticationException e) {
        HttpServletRequest httpServletRequest = RequestUtil.getHttpServletRequest();
        if (ObjectUtils.isEmpty(httpServletRequest)) {
            log.error("认证失败，error", e);
        }
        log.error("认证失败，请求路径:[{}]，原因:{}", httpServletRequest.getRequestURI(), e.getClass().getSimpleName());
        return Response.fail(BasicErrorCode.AUTH_ERROR);
    }
}
