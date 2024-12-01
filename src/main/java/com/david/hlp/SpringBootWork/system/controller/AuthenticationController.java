package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.auth.BaseController;
import com.david.hlp.SpringBootWork.system.service.imp.AuthenticationServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 认证控制器类。
 *
 * 描述：
 * <p>
 * - 负责处理与用户认证相关的操作，包括刷新令牌和用户注册等功能。
 * <p>
 * - 使用 Spring MVC 注解实现 RESTful API 风格的接口。
 * <p>
 * - 使用 Lombok 注解简化依赖注入代码：
 *   - @RequiredArgsConstructor 自动生成包含所有必需依赖项的构造函数。
 * <p>
 * - 继承自 `BaseController`，以便在项目中统一管理公共逻辑。
 */
@RestController
@RequestMapping("/api/v1/auth") // 定义基础路由路径为 "/api/v1/auth"
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class AuthenticationController extends BaseController {

  /**
   * 注入认证服务类。
   *
   * 描述：
   * <p>
   * - 通过 `final` 标注服务依赖，确保依赖项在类初始化时注入。
   * <p>
   * - 认证服务类提供具体的业务逻辑实现，例如刷新令牌。
   */
  private final AuthenticationServiceImp service;

  /**
   * 刷新令牌接口。
   *
   * 描述：
   * <p>
   * - 提供用于刷新用户访问令牌的接口。
   * <p>
   * - 接口接收用户的 HTTP 请求，提取旧的刷新令牌并生成新的访问令牌。
   * <p>
   * - 调用 `AuthenticationServiceImp` 的业务逻辑实现刷新操作。
   *
   * 请求方式：
   * <p>
   * - POST 请求路径为 `/api/v1/auth/refresh-token`。
   *
   * 参数说明：
   * <p>
   * - `HttpServletRequest request`：包含请求头信息，例如旧的刷新令牌。
   * <p>
   * - `HttpServletResponse response`：用于返回新的令牌和响应信息。
   *
   * 异常处理：
   * <p>
   * - 如果发生 I/O 错误，会抛出 `IOException` 异常。
   *
   * @param request  HTTP 请求对象，包含用户的旧令牌等信息。
   * @param response HTTP 响应对象，用于返回新的令牌。
   * @throws IOException 如果发生 I/O 错误。
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