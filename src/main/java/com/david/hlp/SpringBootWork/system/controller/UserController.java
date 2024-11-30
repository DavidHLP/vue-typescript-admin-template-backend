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
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 用户控制器类，处理与用户相关的 API 请求。
 * 提供更改密码的功能。
 */
@Slf4j
@RestController
@RequestMapping("/api/v1") // 将控制器映射到 /api/v1/users 路径
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class UserController extends BaseController {

    // 注入 AuthenticationService 服务类，用于处理具体的认证逻辑
    private final AuthenticationServiceImp service;

    private final UserServiceImp userServiceImp;

    private final MailServiceImp mailService;

    /**
     * 处理更改密码的请求。
     *
     * @param request 包含用户当前密码、新密码和确认密码的请求体。
     * @param connectedUser 当前已认证的用户信息，由 Spring Security 提供的 Principal 对象表示。
     * @return 返回 200 OK 的响应，表示密码更改成功。
     */
    @PatchMapping // 使用 HTTP PATCH 方法，用于部分更新资源
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request, // 从请求体中获取更改密码的请求数据
            Principal connectedUser // 获取当前连接用户的信息
    ) {
        // 调用服务层处理密码更改的逻辑
        userServiceImp.changePassword(request, connectedUser);
        // 返回成功响应
        return ResponseEntity.ok().build();
    }

    /**
     * 用户认证（登录）接口。
     *
     * @param request 包含用户登录信息的请求体（如用户名和密码）。
     * @return 返回包含认证结果的响应，通常包括用户信息和生成的JWT令牌。
     */
    @PostMapping("/users/login")
    public Result<LogInResult> authenticate(
            @RequestBody AuthenticationRequest request // 从请求体中接收认证数据
    ) {

        System.out.println("request: " + request);

        // 设置 email 字段（如果 username 和 email 是同一个值）
        request.setEmail(request.getUsername());

        // 调用 service 的 authenticate 方法并获取结果
        AuthenticationResponse response = service.authenticate(request);

        return Result.<LogInResult>builder()
                .code(20000L)
                .message("登录成功")
                .data(LogInResult.builder().accessToken(response.getAccessToken()).build())
                .build();
    }

    @PostMapping("/users/info")
    public Result<UserInfo> getUserInfo(){

        return Result.<UserInfo>builder().data(userServiceImp.getUser(getCurrentUsername())).code(20000L).build();
    }

    /**
     * 用户注册接口。
     *
     * @param request 包含用户注册信息的请求体（如用户名、密码、邮箱等）。
     * @return 返回包含注册结果的响应，通常包括用户信息和生成的JWT令牌。
     */
    @PostMapping("/users/register")
    public Result<AuthenticationResponse> register(
            @RequestBody RegisterRequest request // 从请求体中接收注册数据
    ) {
        // 调用 service 的 register 方法并返回注册结果
        return Result.ok(service.register(request));
    }

    @GetMapping("/users")
    public Result<ResponsePage<UserInfo>> getUsers(@RequestParam(value = "name") String name) {
        return Result.ok(userServiceImp.getUsers(name));
    }

    // 发送邮件请求
    @GetMapping("/users/sendcode/{email}")
    public Result<Void> sendMail(@PathVariable("email") String email) {
        System.out.println("email: " + email);
        return Result.ok(null,mailService.sendMail(email));
    }
}