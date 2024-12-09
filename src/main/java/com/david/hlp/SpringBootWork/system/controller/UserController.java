package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.entity.LogInResult;
import com.david.hlp.SpringBootWork.system.entity.Result;
import com.david.hlp.SpringBootWork.system.entity.UserInfo;
import com.david.hlp.SpringBootWork.system.requestentity.AuthenticationRequest;
import com.david.hlp.SpringBootWork.system.requestentity.ChangePasswordRequest;
import com.david.hlp.SpringBootWork.system.requestentity.RegisterRequest;
import com.david.hlp.SpringBootWork.system.responsentity.AuthenticationResponse;
import com.david.hlp.SpringBootWork.system.responsentity.ResponsePage;
import com.david.hlp.SpringBootWork.system.service.imp.AuthenticationServiceImp;
import com.david.hlp.SpringBootWork.system.service.imp.MailServiceImp;
import com.david.hlp.SpringBootWork.system.service.imp.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * 用户控制器类。
 *
 * 描述：
 * <p>
 * - 提供与用户相关的 RESTful API 接口。
 * <p>
 * - 包括用户认证（登录）、注册、更改密码、查询用户信息等功能。
 * <p>
 * - 使用 Lombok 注解简化代码：
 *   - @RequiredArgsConstructor 自动生成构造函数，注入必要的依赖。
 *   - @Slf4j 提供日志记录功能。
 */
@Slf4j
@RestController
@RequestMapping("/api/v1") // 将控制器映射到 /api/v1 路径
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class UserController extends BaseController {

    /**
     * 注入认证服务类，用于处理用户认证相关逻辑。
     */
    private final AuthenticationServiceImp service;

    /**
     * 注入用户服务类，用于处理用户数据的业务逻辑。
     */
    private final UserServiceImp userServiceImp;

    /**
     * 注入邮件服务类，用于发送验证码邮件和处理邮件相关业务。
     */
    private final MailServiceImp mailService;

    /**
     * 用户更改密码接口。
     * <p>
     * 描述：
     * - 用户提交当前密码和新密码，系统验证后完成密码更新。
     * <p>
     * 请求方式：
     * - PATCH 请求路径为 `/api/v1/users/password`。
     * <p>
     * 参数：
     * - `request` 包含用户当前密码、新密码和确认密码的请求体。
     * - `connectedUser` 当前已认证的用户，由 Spring Security 提供的 Principal 对象表示。
     * <p>
     * 返回值：
     * - 返回 200 OK 的响应，表示密码更改成功。
     */
    @PatchMapping("/users/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userServiceImp.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    /**
     * 用户认证（登录）接口。
     * <p>
     * 描述：
     * - 用户提交用户名和密码，系统验证后返回 JWT 令牌。
     * <p>
     * 请求方式：
     * - POST 请求路径为 `/api/v1/users/login`。
     * <p>
     * 参数：
     * - `request` 包含用户登录信息的请求体（如用户名和密码）。
     * <p>
     * 返回值：
     * - 包含登录结果的响应，通常包括生成的 JWT 令牌。
     */
    @PostMapping("/users/login")
    public Result<LogInResult> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        request.setEmail(request.getUsername());
        AuthenticationResponse response = service.authenticate(request);
        return Result.<LogInResult>builder()
                .code(20000L)
                .message("登录成功")
                .data(LogInResult.builder().accessToken(response.getAccessToken()).build())
                .build();
    }

    /**
     * 获取用户详细信息接口。
     * <p>
     * 描述：
     * - 返回当前登录用户的详细信息。
     * <p>
     * 请求方式：
     * - POST 请求路径为 `/api/v1/users/info`。
     * <p>
     * 返回值：
     * - 包含用户信息的响应。
     */
    @PostMapping("/users/info")
    public Result<UserInfo> getUserInfo() {
        return Result.<UserInfo>builder()
                .data(userServiceImp.getUser(getCurrentUsername()))
                .code(20000L)
                .build();
    }

    /**
     * 用户注册接口。
     * <p>
     * 描述：
     * - 用户提交注册信息，系统验证后完成注册。
     * <p>
     * 请求方式：
     * - POST 请求路径为 `/api/v1/users/register`。
     * <p>
     * 参数：
     * - `request` 包含用户注册信息的请求体（如用户名、密码、邮箱等）。
     * <p>
     * 返回值：
     * - 包含注册结果的响应，通常包括用户信息和生成的 JWT 令牌。
     */
    @PostMapping("/users/register")
    public Result<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return Result.ok(service.register(request));
    }

    /**
     * 用户查询接口。
     * <p>
     * 描述：
     * - 按用户名模糊查询用户列表。
     * <p>
     * 请求方式：
     * - GET 请求路径为 `/api/v1/users`。
     * <p>
     * 参数：
     * - `name` 用户名的查询关键字。
     * <p>
     * 返回值：
     * - 包含用户信息列表的分页响应。
     */
    @GetMapping("/users")
    public Result<ResponsePage<UserInfo>> getUsers(@RequestParam(value = "name") String name) {
        return Result.ok(userServiceImp.getUsers(name));
    }

    /**
     * 发送验证码邮件接口。
     * <p>
     * 描述：
     * - 系统向指定邮箱发送验证码，用于密码重置或其他验证。
     * <p>
     * 请求方式：
     * - GET 请求路径为 `/api/v1/users/sendcode/{email}`。
     * <p>
     * 参数：
     * - `email` 接收验证码的邮箱地址。
     * <p>
     * 返回值：
     * - 包含发送结果的响应。
     */
    @GetMapping("/users/sendcode/{email}")
    public Result<Void> sendMail(@PathVariable("email") String email) {
        return Result.ok(null, mailService.sendMail(email));
    }
}