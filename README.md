# Matrix 项目结构与配置信息

## 1. 核心环境配置

### Java 环境
- Java 版本: 21
- Maven 编译器源版本: 21
- Maven 编译器目标版本: 21

### 编码配置
- 项目构建源编码: UTF-8
- 项目报告输出编码: UTF-8

### 核心框架版本
- Spring Boot 版本: 3.5.4
- Spring Cloud 版本: 2025.0.0
- Spring Framework 版本: 6.2.9
- Spring Security 版本: 6.5.2

### gRPC 和 Protobuf 版本
- gRPC 版本: 1.74.0
- Protobuf 版本: 4.31.1

## 2. 核心依赖及版本

### Spring 生态依赖
- spring-amqp.version: 3.2.6
- spring-authorization-server.version: 1.5.1
- spring-batch.version: 5.2.2
- spring-data-bom.version: 2025.0.2
- spring-framework.version: 6.2.9
- spring-graphql.version: 1.4.1
- spring-hateoas.version: 2.5.1
- spring-integration.version: 6.5.1
- spring-kafka.version: 3.3.8
- spring-ldap.version: 3.3.2
- spring-pulsar.version: 1.2.8
- spring-restdocs.version: 3.0.4
- spring-retry.version: 2.0.12
- spring-security.version: 6.5.2
- spring-session.version: 3.5.1
- spring-ws.version: 4.1.1

### 第三方依赖
- Lombok 版本: 1.18.38
- Mapstruct 版本: 1.6.3
- R2DBC PostgreSQL 版本: 1.0.7.RELEASE
- Flyway 版本: 11.10.1
- Mockito 版本: 5.17.0

## 3. 项目模块结构和父子关系

```
matrix (根模块)
├── matrix-shared (共享模块)
│   ├── matrix-shared-data
│   ├── matrix-shared-test
│   ├── matrix-shared-common
│   ├── matrix-shared-grpc (gRPC共享模块)
│   │   ├── matrix-shared-grpc-client
│   │   ├── matrix-shared-grpc-server
│   │   └── matrix-shared-grpc-base
│   ├── matrix-shared-snowflake-id-generator
│   └── matrix-shared-logging
├── matrix-gateway
├── matrix-auth
├── matrix-user (用户服务模块)
│   ├── matrix-user-api
│   ├── matrix-user-data
│   ├── matrix-user-grpc-provider
│   ├── matrix-user-consumer
│   └── matrix-user-grpc-proto
├── matrix-room
├── matrix-message
├── matrix-websocket
├── matrix-federation
└── matrix-flyway
```

### 模块依赖关系说明

1. **根模块 (matrix)**:
    - 定义了所有子模块
    - 管理全局依赖版本
    - 配置统一的构建插件

2. **共享模块 (matrix-shared)**:
    - 包含多个子模块，提供通用功能
    - matrix-shared-grpc 提供 gRPC 基础设施
    - matrix-shared-snowflake-id-generator 提供 ID 生成服务

3. **用户服务模块 (matrix-user)**:
    - matrix-user-grpc-proto 定义 gRPC 接口和消息
    - matrix-user-consumer 依赖 matrix-user-grpc-proto
    - 各模块之间通过 gRPC 进行通信

4. **网关模块 (matrix-gateway)**:
    - 依赖 Spring Cloud Gateway
    - 作为整个系统的入口

所有模块都继承自根模块 matrix，共享统一的依赖版本管理和构建配置。这种结构有利于统一管理依赖版本，避免版本冲突，并提供良好的模块化架构。
