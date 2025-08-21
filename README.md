# Matrix 项目配置信息

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
- Protobuf 编译器版本: 4.32.0

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
- Guava 版本: 33.4.8-jre
- R2DBC PostgreSQL 版本: 1.0.7.RELEASE
- Flyway 版本: 11.10.1 (matrix-flyway 模块中使用 11.11.0)
- Mockito 版本: 5.17.0
- Redisson 版本: 3.50.0

### 云原生相关
- Spring Cloud Kubernetes 版本: 3.2.0
- Fabric8 Kubernetes Client 版本: 7.0.1

## 3. 项目模块结构和父子关系
```azure
matrix (根模块)
    ├── matrix-shared (共享模块)
    │   ├── matrix-shared-common
    │   ├── matrix-shared-data
    │   ├── matrix-shared-test
    │   ├── matrix-shared-grpc (gRPC共享模块)
    │   │   ├── matrix-shared-grpc-base
    │   │   ├── matrix-shared-grpc-client
    │   │   └── matrix-shared-grpc-server
    │   ├── matrix-shared-snowflake-id-generator
    │   ├── matrix-shared-logging
    │   ├── matrix-shared-cloud (云服务模块)
    │   │   ├── matrix-shared-cloud-discovery-client
    │   │   └── matrix-shared-cloud-discovery-server
    │   └── matrix-shared-webflux
    ├── matrix-gateway
    ├── matrix-auth
    ├── matrix-user (用户服务模块)
    │   ├── matrix-user-api
    │   ├── matrix-user-data
    │   ├── matrix-user-grpc-proto
    │   ├── matrix-user-grpc-provider
    │   ├── matrix-user-consumer
    │   └── matrix-user-grpc-consumer
    ├── matrix-room
    ├── matrix-message
    ├── matrix-websocket
    ├── matrix-federation
    └── matrix-flyway
```
## 4. 模块依赖关系说明

### 根模块 (matrix)
- 定义了所有子模块
- 管理全局依赖版本
- 配置统一的构建插件

### 共享模块 (matrix-shared)
- **matrix-shared-data**: 提供数据库访问功能，依赖于 Spring WebFlux、Spring Data R2DBC 和 PostgreSQL R2DBC 驱动
- **matrix-shared-grpc**: gRPC 基础设施模块
    - **matrix-shared-grpc-base**: 包含 gRPC 核心依赖，如 grpc-netty-shaded、grpc-protobuf、grpc-stub 等
    - **matrix-shared-grpc-client**: gRPC 客户端功能
    - **matrix-shared-grpc-server**: gRPC 服务端功能
- **matrix-shared-snowflake-id-generator**: 提供分布式 ID 生成服务
- **matrix-shared-cloud**: 云服务相关功能
    - **matrix-shared-cloud-discovery-server**: 服务发现服务端
    - **matrix-shared-cloud-discovery-client**: 服务发现客户端

### 用户服务模块 (matrix-user)
- **matrix-user-grpc-proto**: 定义 gRPC 接口和消息协议，依赖 matrix-shared-grpc-base
- **matrix-user-data**: 用户数据访问模块，依赖 matrix-shared-data
- **matrix-user-grpc-provider**: 提供 gRPC 服务实现
- **matrix-user-consumer**: 用户服务消费者
- **matrix-user-grpc-consumer**: gRPC 消费者实现
- **matrix-user-api**: 用户服务 API 接口定义

### 网关模块 (matrix-gateway)
- 依赖 Spring Cloud Gateway
- 作为整个系统的入口
- 依赖 matrix-shared-webflux 和 matrix-shared-cloud-discovery-server

### 认证模块 (matrix-auth)
- 提供认证服务功能
- 依赖 Spring Security 和 JWT 库
- 使用 R2DBC PostgreSQL 进行数据存储

### 数据库迁移模块 (matrix-flyway)
- 管理数据库迁移脚本
- 使用 Flyway 进行数据库版本控制
- 依赖 PostgreSQL JDBC 驱动

所有模块都继承自根模块 matrix，共享统一的依赖版本管理和构建配置。这种结构有利于统一管理依赖版本，避免版本冲突，并提供良好的模块化架构。