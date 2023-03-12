package com.bewind.springbootstart.exception;

import com.bewind.springbootstart.common.ApiCode;

public class ApiException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.code = apiCode.getCode();
    }

    public ApiException(ApiCode apiCode, String message) {
        super(message);
        this.code = apiCode.getCode();
    }

    public int getCode() {
        return code;
    }

}
