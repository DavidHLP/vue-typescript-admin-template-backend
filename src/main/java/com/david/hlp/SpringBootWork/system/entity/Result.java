package com.david.hlp.SpringBootWork.system.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用结果响应类。
 *
 * 描述：
 * <p>
 * - 用于封装通用的响应结果，支持状态码、提示信息和数据返回。
 * <p>
 * - 支持泛型 (T)，可以适配不同类型的响应数据。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @Data 自动生成 Getter、Setter、toString、equals 和 hashCode 方法。
 *   - @Builder 提供构建器模式，用于灵活创建对象实例。
 *   - @NoArgsConstructor 自动生成无参构造函数。
 *   - @AllArgsConstructor 自动生成全参构造函数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码。
     *
     * 描述：
     * - 标识操作结果的状态码，例如：
     *   - 200 表示成功。
     *   - 401 表示未授权。
     *   - 404 表示资源未找到。
     *   - 500 表示服务器错误。
     */
    private Long code;

    /**
     * 提示信息。
     *
     * 描述：
     * - 用于向前端返回消息内容，便于用户理解或开发者调试。
     */
    private String message;

    /**
     * 数据字段。
     *
     * 描述：
     * - 用于返回具体的响应数据。
     * - 支持泛型，适配不同的数据类型。
     */
    private T data;

    // 静态方法区域

    /**
     * 成功的静态构建方法。
     *
     * 描述：
     * - 返回一个带有成功状态码和数据的 Result 对象。
     *
     * @param data 响应数据。
     * @return 包含成功信息的 Result 对象。
     */
    public static <T> Result<T> ok(T data) {
        return Result.<T>builder()
                .code(20000L)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * 成功的静态构建方法。
     *
     * 描述：
     * - 返回一个带有成功状态码、自定义消息和数据的 Result 对象。
     *
     * @param data 响应数据。
     * @param message 提示信息。
     * @return 包含成功信息的 Result 对象。
     */
    public static <T> Result<T> ok(T data, String message) {
        return Result.<T>builder()
                .code(20000L)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 错误的静态构建方法。
     *
     * 描述：
     * - 返回固定错误信息的 Result 对象，用于无数据返回的场景。
     * - 以下方法根据不同错误场景提供具体的错误消息和状态码。
     */
    public static <T> Result<T> error50001() {
        return Result.<T>builder()
                .code(50001L)
                .message("invalid access token")
                .data(null)
                .build();
    }

    public static <T> Result<T> error50002() {
        return Result.<T>builder()
                .code(50002L)
                .message("already login in other place")
                .data(null)
                .build();
    }

    public static <T> Result<T> error50003() {
        return Result.<T>builder()
                .code(50003L)
                .message("access token expired")
                .data(null)
                .build();
    }

    public static <T> Result<T> error50004() {
        return Result.<T>builder()
                .code(50004L)
                .message("invalid user (user not exist)")
                .data(null)
                .build();
    }

    public static <T> Result<T> error50005() {
        return Result.<T>builder()
                .code(50005L)
                .message("username or password is incorrect")
                .data(null)
                .build();
    }

    /**
     * 错误的静态构建方法（带自定义状态码）。
     *
     * 描述：
     * - 支持动态设置状态码和提示信息，用于返回特定的错误信息。
     *
     * @param code 自定义状态码。
     * @param message 提示信息。
     * @return 包含错误信息的 Result 对象。
     */
    public static <T> Result<T> error(Long code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 404 Not Found 的静态构建方法。
     *
     * 描述：
     * - 用于构建资源未找到的响应结果。
     *
     * @param message 提示信息。
     * @return 包含 404 状态码和提示信息的 Result 对象。
     */
    public static <T> Result<T> notFound(String message) {
        return Result.<T>builder()
                .code(404L)
                .message(message)
                .data(null)
                .build();
    }

    /**
     * 404 Not Found 的静态构建方法（默认提示信息）。
     *
     * 描述：
     * - 返回默认提示信息 "not found" 的 Result 对象。
     *
     * @return 包含 404 状态码和默认提示信息的 Result 对象。
     */
    public static <T> Result<T> notFound() {
        return Result.<T>builder()
                .code(404L)
                .message("not found")
                .data(null)
                .build();
    }
}