package com.cn.crieff.exception;

/**
 * 业务错误异常
 */
public class BizException extends RuntimeException {

    private int code;

    private String message;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ErrorCode serverCode) {
        super(serverCode.getMessage());
        this.code = serverCode.getCode();
        this.message = serverCode.getMessage();
    }

    public BizException(ErrorCode serverCode, String message) {
        super(message);
        this.code = serverCode.getCode();
        this.message = message;
    }

    public static BizException ofMessage(ErrorCode serverCode, String overrideMessage) {
        return new BizException(serverCode, overrideMessage);
    }

    public static BizException of(ErrorCode serverCode) {
        return new BizException(serverCode);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
