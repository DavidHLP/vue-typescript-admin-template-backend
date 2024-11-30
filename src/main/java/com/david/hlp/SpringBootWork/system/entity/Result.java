package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码，标识操作结果（如200成功，401未授权）
     */
    private Long code;

    /**
     * 提示信息，用于前端显示或调试
     */
    private String message;

    /**
     * 泛型数据字段，用于返回具体的响应数据
     */
    private T data;

    /**
     * 成功的静态构建方法
     */
    public static <T> Result<T> ok(T data) {
        return Result.<T>builder()
                .code(20000L)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * 成功的静态构建方法
     */
    public static <T> Result<T> ok(T data, String message) {
        return Result.<T>builder()
                .code(20000L)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 错误的静态构建方法
     */
    public static <T> Result<T> error50001() {
        return Result.<T>builder()
                .code(50001L)
                .message("invalid access token")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法
     */
    public static <T> Result<T> error50002() {
        return Result.<T>builder()
                .code(50002L)
                .message("already login in other place")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法
     */
    public static <T> Result<T> error50003() {
        return Result.<T>builder()
                .code(50003L)
                .message("access token expired")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法
     */
    public static <T> Result<T> error50004() {
        return Result.<T>builder()
                .code(50004L)
                .message("invalid user (user not exist")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法
     */
    public static <T> Result<T> error50005() {
        return Result.<T>builder()
                .code(50005L)
                .message("username or password is incorrect")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法（带自定义状态码）
     */
    public static <T> Result<T> error(Long code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 404 Not Found 的静态构建方法
     */
    public static <T> Result<T> notFound(String message) {
        return Result.<T>builder()
                .code(404L)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 404 Not Found 的静态构建方法
     */
    public static <T> Result<T> notFound() {
        return Result.<T>builder()
                .code(404L)
                .message("not found")
                .data(null)
                .build();
    }
}
