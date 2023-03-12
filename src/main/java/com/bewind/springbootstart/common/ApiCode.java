package com.bewind.springbootstart.common;

public enum ApiCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 失败
     */
    FAILED(-1, "操作失败"),

    /**
     * 参数校验错误
     */
    VALIDATE_FAILED(404, "参数检验失败"),

    /**
     * 执行错误
     */
    PARAM_ERROR(444,"执行错误");

    private final Integer code;
    private final String message;

    ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiErrorCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
