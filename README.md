# vue-typescript-admin-template-backend

## 总览

[vue-typescript-admin-template-backend](https://github.com/DavidHLP/vue-typescript-admin-template-backend) 是一个vue-typescript-admin-template的后端实现系统，它基于 Java 、 Spring 、 Spring Security 的系统

## 功能

```txt
- 登录 / 注销

- 权限验证
  - Jwt
  - 多Token登录
  - Redis 二次验证
  - 指令权限
  - 权限配置
```

## 目录结构

```bash
├── java
│   └── com
│       └── david
│           └── hlp
│               └── SpringBootWork
│                   ├── demo
│                   │   ├── controller
│                   │   │   ├── DraggableTableController.java
│                   │   │   └── PageviewsController.java
│                   │   ├── entity
│                   │   │   ├── ArticleWrapper.java
│                   │   │   └── DraggableTable.java
│                   │   ├── mapper
│                   │   │   └── DraggableTableMapper.java
│                   │   ├── Repository
│                   │   │   └── DraggableTableRepository.java
│                   │   ├── service
│                   │   │   └── DraggableTableService.java
│                   │   └── util
│                   │       └── DraggableTableResult.java
│                   ├── runner
│                   │   ├── DataLoaderRunner.java
│                   │   ├── PermissionRunner.java
│                   │   ├── RolePermissionRunner.java
│                   │   ├── RoleRunner.java
│                   │   └── UserRunner.java
│                   ├── SpringBootWorkApplication.java
│                   └── system
│                       ├── auditing
│                       │   └── ApplicationAuditAware.java
│                       ├── auth
│                       │   ├── ApplicationConfig.java
│                       │   ├── BaseController.java
│                       │   ├── JwtAuthenticationFilter.java
│                       │   ├── JwtService.java
│                       │   ├── LogoutService.java
│                       │   ├── SecurityConfiguration.java
│                       │   └── WebConfig.java
│                       ├── controller
│                       │   ├── AdminController.java
│                       │   ├── AuthenticationController.java
│                       │   ├── RoleController.java
│                       │   ├── SpringMailController.java
│                       │   └── UserController.java
│                       ├── entity
│                       │   ├── LogInResult.java
│                       │   ├── Permission.java
│                       │   ├── Result.java
│                       │   ├── Role.java
│                       │   ├── RolePermission.java
│                       │   ├── UserInfo.java
│                       │   └── User.java
│                       ├── enumentity
│                       │   ├── DefaultRole.java
│                       │   └── DefaultRolePermission.java
│                       ├── Main.java
│                       ├── mapper
│                       │   ├── PermissionMapper.java
│                       │   ├── RoleMapper.java
│                       │   └── UserMapper.java
│                       ├── Repository
│                       │   ├── PermissionRepository.java
│                       │   ├── RolePermissionRepository.java
│                       │   ├── RoleRepository.java
│                       │   └── UserRepository.java
│                       ├── requestentity
│                       │   ├── AuthenticationRequest.java
│                       │   ├── ChangePasswordRequest.java
│                       │   └── RegisterRequest.java
│                       ├── responsentity
│                       │   ├── AuthenticationResponse.java
│                       │   └── ResponsePage.java
│                       ├── service
│                       │   └── imp
│                       │       ├── AuthenticationServiceImp.java
│                       │       ├── MailServiceImp.java
│                       │       ├── RoleServiceImp.java
│                       │       └── UserServiceImp.java
│                       ├── token
│                       │   ├── Token.java
│                       │   ├── TokenRepository.java
│                       │   └── TokenType.java
│                       └── util
│                           └── RedisCache.java
└── resources
    ├── application-local.yml
    ├── application.yml
    ├── draggable_table_data_detailed.html.json
    ├── mapper
    │   ├── DraggableTableMapper.xml
    │   └── UserMapper.xml
    ├── static
    └── templates
```
