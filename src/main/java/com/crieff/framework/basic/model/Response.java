package com.crieff.framework.basic.model;


import com.crieff.framework.exception.BasicErrorCode;
import com.crieff.framework.exception.BizException;
import com.crieff.framework.exception.ErrorCode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @description:
 * @author: aKuang
 * @time: 10/26/22 11:10 AM
 */
@Data
@Slf4j
@NoArgsConstructor
public class Response<T> {

    private String msg;
    private int code;
    private T data;

    public Response(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public Response(String msg, int code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    /**
     * 返回成功
     */
    public static <V> Response<V> success() {
        return identity(BasicErrorCode.SUCCESS);
    }

    public static <V> Response<V> success(V data) {
        return identity(BasicErrorCode.SUCCESS, data);
    }

    public static <V> Response<V> success(String msg, V data) {
        return identity(BasicErrorCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 返回失败
     */
    public static <V> Response<V> fail() {
        return identity(BasicErrorCode.SERVER_ERROR);
    }

    public static <V> Response<V> fail(int code, String msg) {
        return identity(code, msg);
    }

    public static <V> Response<V> fail(ErrorCode errorCode) {
        return identity(errorCode);
    }

    /**
     * 返回失败
     */
    public static <V> Response<V> fail(Exception e) {
        return identity(BasicErrorCode.SERVER_ERROR.getCode(), e.getClass().getSimpleName());
    }

    /**
     * 返回失败
     */
    public static <V> Response<V> fail(BizException e) {
        return identity(e.getCode(), e.getMessage());
    }

    /**
     * 全局异常处理
     */
    public static <V> Response<V> globalFail(HttpServletRequest httpServletRequest, Exception e) {
        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.error("业务异常，原因:{}", e.getClass().getSimpleName());
            return identity(bizException.getCode(), bizException.getMessage());
        } else if (e instanceof AuthenticationException) {
            return identity(BasicErrorCode.AUTH_ERROR);
        }
        log.error("系统异常，请求路径:{}", httpServletRequest.getRequestURI(), e);
        return identity(BasicErrorCode.SERVER_ERROR.getCode(), e.getClass().getSimpleName());
    }


    /**
     * 根据错误码自定义返回
     */
    private static <V> Response<V> identity(ErrorCode errorCode) {
        return new Response<>(errorCode.getMessage(), errorCode.getCode());

    }

    /**
     * 根据错误码自定义data返回
     */
    private static <V> Response<V> identity(ErrorCode errorCode, V data) {
        return new Response<>(errorCode.getMessage(), errorCode.getCode(), data);
    }

    /**
     * 根据msg、code自定义返回
     */
    private static <V> Response<V> identity(int code, String msg) {
        return new Response<>(msg, code);
    }


    /**
     * 根据msg、code自定义data返回
     */
    private static <V> Response<V> identity(int code, String msg, V data) {
        return new Response<>(msg, code, data);
    }


    @Data
    @Getter
    @NoArgsConstructor
    public static class Page<E> {
        /**
         * 总数
         */
        @Setter
        private int total;

        /**
         * 列表数据
         */
        private Collection<E> list;

        public Page(Collection<E> list, int total) {
            this.list = list;
            this.total = total;
        }

        public Page(Collection<E> list, long total) {
            this.list = list;
            this.total = (int) total;
        }

        public void setTotal(long total) {
            this.total = (int) total;
        }

        @Override
        public String toString() {
            return "{total=" + total + ", list=" + list + "}";
        }
    }
}
