package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.service.imp.AuthenticationServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 认证控制器类，处理用户认证、注册和刷新令牌等操作。
 */
@RestController
@RequestMapping("/api/v1/auth") // 定义基础路由路径为 "/api/v1/auth"
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class AuthenticationController extends BaseController {

  // 注入 AuthenticationService 服务类，用于处理具体的认证逻辑
  private final AuthenticationServiceImp service;

  /**
   * 刷新令牌接口。
   *
   * @param request  HTTP请求对象，包含用户的旧令牌等信息。
   * @param response HTTP响应对象，用于返回新的令牌。
   * @throws IOException 如果发生I/O错误。
   */
  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request, // 注入 HttpServletRequest 以获取请求相关信息
          HttpServletResponse response // 注入 HttpServletResponse 用于返回响应
  ) throws IOException {
    // 调用 service 的 refreshToken 方法执行刷新令牌的逻辑
    service.refreshToken(request, response);
  }
}
