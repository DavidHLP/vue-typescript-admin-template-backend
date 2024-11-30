package com.david.hlp.SpringBootWork.system.controller;

import com.david.hlp.SpringBootWork.system.service.imp.RoleServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role") // 将控制器映射到 /api/v1/role 路径
@RequiredArgsConstructor // 自动生成构造函数，注入必要的依赖
public class RoleController {
    private final RoleServiceImp roleServiceImp;

}
