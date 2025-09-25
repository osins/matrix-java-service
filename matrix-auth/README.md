# Matrix 认证服务模块

## 模块结构

本模块是 Matrix 项目中的认证服务模块，包含以下子模块：

### matrix-auth-api
- 提供认证相关的 REST API 接口定义
- 依赖 matrix-shared-webflux 模块

### matrix-auth-data
- 处理认证相关的数据访问操作
- 依赖 matrix-shared-data 模块

### matrix-auth-security
- 实现权限控制和安全服务
- 依赖 Spring Security 和 JWT 相关库

### matrix-auth-grpc-provider
- 提供认证服务的 gRPC 服务端实现
- 依赖 matrix-shared-grpc-server 模块

### matrix-auth-grpc-consumer
- 实现认证服务的 gRPC 客户端
- 依赖 matrix-shared-grpc-client 模块

## 功能说明

该模块将实现 Matrix 协议的认证和权限控制功能，包括：
- 用户注册和登录
- JWT 令牌生成和验证
- OAuth 2.0/OIDC 支持
- 基于角色的访问控制 (RBAC)
- 细粒度权限管理
- 服务间安全通信

## 与 Matrix 生态系统对接

要与现有的 Matrix 客户端和服务器对接，需要：
1. 实现 Matrix Client-Server API 的认证相关端点
2. 遵循 Matrix 协议的数据格式和通信规范
3. 支持标准的认证流程和令牌机制