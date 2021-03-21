package com.xxkun.relayserver_udp.common;

/**
 * 请求返回码枚举汇总
 * Create by xxkun on 2020/12/17
 */
public enum ResultCode implements IResultCode {

    SUCCESS(200, "请求成功"),
    FAILED(500, "请求失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    private final long code;
    private final String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
