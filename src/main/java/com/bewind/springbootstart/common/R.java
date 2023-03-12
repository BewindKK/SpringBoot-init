package com.bewind.springbootstart.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

import static javax.management.remote.JMXConnectionNotification.FAILED;

@Data
@NoArgsConstructor
public class R<T> implements Serializable {
    /**
     * 业务状态码
     */
    private int code;
    /**
     * 结果集
     */
    private T data;
    /**
     * 接口描述
     */
    private String message;

    /**
     * 全参
     *
     * @param code    业务状态码
     * @param message 描述
     * @param data    结果集
     */
    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }



    /**
     * 成功
     * @return {code:200,message:操作成功,data:自定义}
     */
    public static <T> R<T> success() {
        return new R<T>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功
     *
     * @param data 结果集
     * @return {code:200,message:操作成功,data:自定义}
     */
    public static <T> R<T> success(T data) {
        return new R<T>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param data    结果集
     * @param message 自定义提示信息
     * @return {code:200,message:自定义,data:自定义}
     */
    public static <T> R<T> success(T data, String message) {
        return new R<T>(ApiCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> failed() {
        return new R<T>(ApiCode.FAILED.getCode(), ApiCode.FAILED.getMessage(), null);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> failed(int code,String message) {
        return new R<T>(code,message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     * @return {code:枚举ApiErrorCode取,message:自定义,data:null}
     */
    public static <T> R<T> failed(String message) {
        return new R<T>(ApiCode.FAILED.getCode(), message, null);
    }




}
