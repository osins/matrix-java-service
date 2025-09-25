# Matrix 分布式系统平台

Matrix 是一个基于 Spring Cloud 的现代化分布式系统平台，采用微服务架构设计，支持 gRPC 通信、OAuth2 认证、服务发现等企业级特性。

## 目录

- [项目概述](#项目概述)
- [技术栈](#技术栈)
- [系统架构](#系统架构)
- [模块结构](#模块结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [模块详解](#模块详解)
- [开发指南](#开发指南)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

## 项目概述

Matrix 是一个遵循 Matrix 协议的分布式系统平台，旨在提供高性能、可扩展的微服务解决方案。项目采用模块化设计，每个模块都有明确的职责和接口，便于维护和扩展。

主要特性：
- 基于 Spring Boot 3.5.4 和 Spring Cloud 2025.0.0
- 支持 gRPC 高性能通信
- 集成 OAuth2 认证和授权服务
- 使用 R2DBC 实现响应式数据访问
- 支持服务发现和负载均衡
- 提供分布式 ID 生成器
- 集成 Flyway 数据库迁移工具

## 技术栈

### 核心框架
- Java 21
- Spring Boot 3.5.4
- Spring Cloud 2025.0.0
- Spring Security 6.5.2
- Spring Authorization Server 1.5.1

### 数据库
- PostgreSQL (主数据库)
- R2DBC PostgreSQL 1.0.7.RELEASE (响应式数据库驱动)

### 通信协议
- gRPC 1.74.0
- Protocol Buffers 4.31.1
- RESTful API

### 其他技术
- Maven 3.9+
- Lombok 1.18.38
- MapStruct 1.6.3
- Guava 33.4.8-jre
- Flyway 11.10.1
- Mockito 5.17.0

## 系统架构

Matrix 采用微服务架构，各服务之间通过 gRPC 或 RESTful API 进行通信。系统包含以下核心组件：

1. **网关服务 (matrix-gateway)** - 统一入口，处理路由、认证等
2. **认证服务 (matrix-auth)** - 提供 OAuth2 认证和权限管理
3. **用户服务 (matrix-user)** - 管理用户信息和相关业务
4. **共享模块 (matrix-shared)** - 提供通用工具和服务
5. **客户端服务 (matrix-client-server)** - 处理客户端相关业务
6. **数据库迁移 (matrix-flyway)** - 管理数据库版本和迁移

## 模块结构

```
matrix (根模块)
├── matrix-dependencies              # 依赖版本管理
├── matrix-shared                    # 共享模块
│   ├── matrix-shared-api            # API 接口定义
│   ├── matrix-shared-cache          # 缓存组件
│   ├── matrix-shared-cloud          # 云服务组件
│   │   ├── matrix-shared-cloud-discovery-client  # 服务发现客户端
│   │   └── matrix-shared-cloud-discovery-server  # 服务发现服务端
│   ├── matrix-shared-common         # 通用工具类
│   ├── matrix-shared-data           # 数据访问组件
│   ├── matrix-shared-grpc           # gRPC 基础设施
│   │   ├── matrix-shared-grpc-base     # gRPC 基础依赖
│   │   ├── matrix-shared-grpc-client   # gRPC 客户端
│   │   └── matrix-shared-grpc-server   # gRPC 服务端
│   ├── matrix-shared-logging        # 日志组件
│   ├── matrix-shared-sms            # 短信服务
│   ├── matrix-shared-snowflake-id-generator  # 分布式ID生成器
│   ├── matrix-shared-socket         # Socket 通信
│   ├── matrix-shared-test           # 测试组件
│   ├── matrix-shared-tracing        # 链路追踪
│   └── matrix-shared-webflux        # WebFlux 组件
├── matrix-auth                      # 认证服务
│   ├── matrix-auth-api              # 认证服务接口
│   ├── matrix-auth-data             # 认证数据访问
│   ├── matrix-auth-security         # 安全配置
│   ├── matrix-auth-grpc-consumer    # gRPC 认证消费者
│   ├── matrix-auth-grpc-provider    # gRPC 认证提供者
│   ├── matrix-auth-grpc-proto       # gRPC 认证协议定义
│   ├── matrix-auth-oauth2-server    # OAuth2 服务端
│   ├── matrix-auth-role-strategy-manage  # 角色策略管理
│   └── ...                          # 其他认证相关模块
├── matrix-user                      # 用户服务
│   ├── matrix-user-api              # 用户服务接口
│   ├── matrix-user-data             # 用户数据访问
│   ├── matrix-user-consumer         # 用户服务消费者
│   ├── matrix-user-grpc-consumer    # gRPC 用户消费者
│   ├── matrix-user-grpc-provider    # gRPC 用户提供者
│   ├── matrix-user-grpc-proto       # gRPC 用户协议定义
│   └── ...                          # 其他用户相关模块
├── matrix-client-server             # 客户端服务
│   ├── matrix-client-server-api     # 客户端服务接口
│   ├── matrix-client-server-auth    # 客户端认证
│   ├── matrix-client-server-common  # 客户端通用组件
│   └── ...                          # 其他客户端相关模块
├── matrix-gateway                   # 网关服务
└── matrix-flyway                    # 数据库迁移
```

## 环境要求

- Java 21 或更高版本
- Maven 3.9 或更高版本
- Docker (用于运行依赖服务)
- PostgreSQL 数据库

## 快速开始

1. 克隆项目代码：
   ```bash
   git clone <项目地址>
   cd matrix
   ```

2. 启动依赖服务（数据库等）：
   ```bash
   docker-compose up -d
   ```

3. 构建项目：
   ```bash
   mvn clean install
   ```

4. 启动各服务模块：
   ```bash
   mvn spring-boot:run -pl <模块名>
   ```

## 模块详解

### 认证服务 (matrix-auth)
提供完整的认证和授权功能，包括：
- OAuth2 服务端实现
- JWT Token 管理
- 用户权限管理
- 角色策略管理

### 用户服务 (matrix-user)
管理用户相关信息：
- 用户基本信息管理
- 用户数据持久化
- gRPC 服务接口

### 共享模块 (matrix-shared)
提供通用组件和工具：
- 数据访问封装
- gRPC 基础设施
- 缓存管理
- 日志处理
- 分布式 ID 生成器

### 网关服务 (matrix-gateway)
系统统一入口：
- 请求路由
- 负载均衡
- 认证鉴权
- 限流熔断

## 开发指南

### 代码规范
- 遵循 Google Java Style Guide
- 使用 Lombok 简化代码
- 使用 MapStruct 进行对象映射
- 编写单元测试和集成测试

### 新增模块
1. 在根 pom.xml 中添加模块引用
2. 创建模块目录结构
3. 编写模块 pom.xml 文件
4. 实现业务逻辑

### 数据库迁移
使用 Flyway 管理数据库变更：
1. 在 matrix-flyway 模块中添加迁移脚本
2. 脚本命名遵循 V<版本号>__<描述>.sql 格式
3. 运行 mvn flyway:migrate 执行迁移

## 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目。

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 Apache License 2.0 许可证。详情请见 [LICENSE](LICENSE) 文件。