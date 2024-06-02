package com.cn.crieff.exception;

/**
 * @description:
 * @author: aKuang
 * @time: 10/26/22 4:59 PM
 */
public enum BasicErrorCode implements ErrorCode {
    UNKNOWN(0, "未知"),
    SUCCESS(1, "成功"),
    TOO_MANY_REQ(8, "请求太多"),
    AUTH_ERROR(10, "认证失败"),
    SERVER_ERROR(11, "服务端错误"),
    PARAM_INVALID(21, "参数不合法"),
    INSERT_FAIL(31, "创建记录失败"),
    UPDATE_FAIL(32, "更新记录失败"),
    QUERY_FAIL(33, "查询记录失败"),
    DELETE_FAIL(34, "删除记录失败"),
    QUERY_TOO_FAST(35, "您的操作太频繁"),
    INTERFACE_DEPRECATED(40, "访问接口已废弃"),
    ;

    private final int code;

    private final String message;

    BasicErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
