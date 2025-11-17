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
- Flyway 版本: 11.11.0 (matrix-flyway 模块中使用 11.11.0)
- Mockito 版本: 5.17.0
- JWT 版本: 0.12.6 (matrix-auth 模块)

### 云原生相关
- Spring Cloud Kubernetes 版本: 3.2.0
- Fabric8 Kubernetes Client 版本: 7.0.1

## 3. 项目模块结构和父子关系
```mermaid
graph TD
    A[matrix (根模块)] --> B[matrix-00-dependencies]
    A --> C[matrix-shared]
    A --> D[matrix-shared-grpc]
    A --> E[matrix-flyway]
    A --> F[matrix-auth]
    A --> G[matrix-user]
    A --> H[matrix-client-server]
    A --> I[matrix-gateway]
    
    B --> B1[matrix-auth-dependencies]
    B --> B2[matrix-client-dependencies]
    B --> B3[matrix-shared-dependencies]
    B --> B4[matrix-shared-grpc-dependencies]
    B --> B5[matrix-user-dependencies]

    C --> C1[matrix-shared-api]
    C --> C2[matrix-shared-cache]
    C --> C3[matrix-shared-cloud]
    C --> C4[matrix-shared-common]
    C --> C5[matrix-shared-data]
    C --> C6[matrix-shared-grpc]
    C --> C7[matrix-shared-logging]
    C --> C8[matrix-shared-sms]
    C --> C9[matrix-shared-snowflake-id-generator]
    C --> C10[matrix-shared-socket]
    C --> C11[matrix-shared-test]
    C --> C12[matrix-shared-tracing]
    C --> C13[matrix-shared-webflux]

    D --> D1[matrix-shared-grpc-base]
    D --> D2[matrix-shared-grpc-client]
    D --> D3[matrix-shared-grpc-server]

    C3 --> C31[matrix-shared-cloud-discovery-client]
    C3 --> C32[matrix-shared-cloud-discovery-server]

    F --> F1[matrix-auth-api]
    F --> F2[matrix-auth-data]
    F --> F3[matrix-auth-security]
    F --> F4[matrix-auth-grpc-consumer]
    F --> F5[matrix-auth-grpc-provider]
    F --> F6[matrix-auth-grpc-proto]
    F --> F7[matrix-auth-oauth2-server]
    F --> F8[matrix-auth-role-strategy-manage]

    G --> G1[matrix-user-api]
    G --> G2[matrix-user-consumer]
    G --> G3[matrix-user-data]
    G --> G4[matrix-user-grpc-consumer]
    G --> G5[matrix-user-grpc-proto]
    G --> G6[matrix-user-grpc-provider]

    H --> H1[matrix-client-server-api]
    H --> H2[matrix-client-server-auth]
    H --> H3[matrix-client-server-common]
```

## 4. 项目文件结构
```
/Users/shaoyingwang/works/codes/TaskMate/matrix/
├── .gitignore
├── compose.yaml
├── kill-port.sh
├── pom.xml
├── README-SPRING-CLOUD.md
├── README.md
├── .git/
├── .idea/
├── matrix-00-dependencies/
│   ├── pom.xml
│   ├── matrix-auth-dependencies/
│   ├── matrix-client-dependencies/
│   ├── matrix-shared-dependencies/
│   ├── matrix-shared-grpc-dependencies/
│   └── matrix-user-dependencies/
├── matrix-auth/
│   ├── pom.xml
│   ├── README.md
│   ├── matrix-auth-api/
│   ├── matrix-auth-data/
│   ├── matrix-auth-grpc-consumer/
│   ├── matrix-auth-grpc-proto/
│   ├── matrix-auth-grpc-provider/
│   ├── matrix-auth-oauth2-server/
│   ├── matrix-auth-role-strategy-manage/
│   └── matrix-auth-security/
├── matrix-client-server/
│   ├── pom.xml
│   ├── matrix-client-server-api/
│   ├── matrix-client-server-auth/
│   └── matrix-client-server-common/
├── matrix-flyway/
│   ├── pom.xml
│   ├── src/
│   │   └── main/
│   │       └── resources/
│   │           └── db/
│   │               └── migration/
│   │                   ├── V20251114090000__create_database.sql
│   │                   ├── V20251114100001__init_auth_schema.sql
│   │                   └── V20251114100002__init_user_schema.sql
│   └── target/
├── matrix-gateway/
│   ├── pom.xml
│   ├── src/
│   └── target/
├── matrix-shared/
│   ├── pom.xml
│   ├── matrix-shared-api/
│   ├── matrix-shared-cache/
│   ├── matrix-shared-cloud/
│   ├── matrix-shared-common/
│   ├── matrix-shared-data/
│   ├── matrix-shared-grpc/
│   ├── matrix-shared-logging/
│   ├── matrix-shared-sms/
│   ├── matrix-shared-snowflake-id-generator/
│   ├── matrix-shared-socket/
│   ├── matrix-shared-test/
│   ├── matrix-shared-tracing/
│   └── matrix-shared-webflux/
├── matrix-shared-grpc/
│   ├── pom.xml
│   ├── matrix-shared-grpc-base/
│   ├── matrix-shared-grpc-client/
│   └── matrix-shared-grpc-server/
└── matrix-user/
    ├── pom.xml
    ├── matrix-user-api/
    ├── matrix-user-consumer/
    ├── matrix-user-data/
    ├── matrix-user-grpc-consumer/
    ├── matrix-user-grpc-proto/
    └── matrix-user-grpc-provider/
```

## 5. 模块依赖关系说明

### 根模块 (matrix)
- 定义了所有子模块
- 管理全局依赖版本
- 配置统一的构建插件

### 依赖管理模块 (matrix-00-dependencies)
- 包含多个子模块，用于管理不同功能的依赖版本
- matrix-auth-dependencies: 认证模块依赖管理
- matrix-client-dependencies: 客户端服务依赖管理
- matrix-shared-dependencies: 共享模块依赖管理
- matrix-shared-grpc-dependencies: gRPC 共享模块依赖管理
- matrix-user-dependencies: 用户模块依赖管理

### 共享模块 (matrix-shared)
- **matrix-shared-api**: 提供基础 API 定义
- **matrix-shared-cache**: 缓存功能实现
- **matrix-shared-cloud**: 云服务相关功能
    - **matrix-shared-cloud-discovery-client**: 服务发现客户端
    - **matrix-shared-cloud-discovery-server**: 服务发现服务端
- **matrix-shared-common**: 通用工具和基础功能
- **matrix-shared-data**: 数据访问功能，依赖于 Spring WebFlux、Spring Data R2DBC 和 PostgreSQL R2DBC 驱动
- **matrix-shared-grpc**: gRPC 基础设施模块 (注意: 此模块与 matrix-shared-grpc 父模块不同)
- **matrix-shared-logging**: 日志功能模块
- **matrix-shared-sms**: 短信功能模块
- **matrix-shared-snowflake-id-generator**: 分布式 ID 生成器
- **matrix-shared-socket**: Socket 相关功能
- **matrix-shared-test**: 测试工具和基类
- **matrix-shared-tracing**: 分布式追踪功能
- **matrix-shared-webflux**: WebFlux 相关功能

### gRPC 共享模块 (matrix-shared-grpc)
- **matrix-shared-grpc-base**: 包含 gRPC 核心依赖，如 grpc-netty-shaded、grpc-protobuf、grpc-stub 等
- **matrix-shared-grpc-client**: gRPC 客户端功能
- **matrix-shared-grpc-server**: gRPC 服务端功能

### 认证模块 (matrix-auth)
- **matrix-auth-api**: 提供认证相关的 REST API 接口定义，依赖 matrix-shared-webflux 模块
- **matrix-auth-data**: 处理认证相关的数据访问操作，依赖 matrix-shared-data 模块
- **matrix-auth-security**: 实现权限控制和安全服务，依赖 Spring Security 和 JWT 相关库
- **matrix-auth-grpc-provider**: 提供认证服务的 gRPC 服务端实现，依赖 matrix-shared-grpc-server 模块
- **matrix-auth-grpc-consumer**: 实现认证服务的 gRPC 客户端，依赖 matrix-shared-grpc-client 模块
- **matrix-auth-grpc-proto**: 定义认证服务的 gRPC 接口和消息协议
- **matrix-auth-oauth2-server**: OAuth2 服务器实现
- **matrix-auth-role-strategy-manage**: 角色策略管理功能

### 用户服务模块 (matrix-user)
- **matrix-user-api**: 用户服务 API 接口定义
- **matrix-user-consumer**: 用户服务消费者
- **matrix-user-data**: 用户数据访问模块，依赖 matrix-shared-data
- **matrix-user-grpc-consumer**: gRPC 消费者实现
- **matrix-user-grpc-proto**: 定义 gRPC 接口和消息协议，依赖 matrix-shared-grpc-base
- **matrix-user-grpc-provider**: 提供 gRPC 服务实现

### 客户端服务器模块 (matrix-client-server)
- **matrix-client-server-api**: 客户端服务器 API 模块
- **matrix-client-server-auth**: 客户端认证模块
- **matrix-client-server-common**: 客户端通用功能模块

### 网关模块 (matrix-gateway)
- 依赖 Spring Cloud Gateway
- 作为整个系统的入口
- 依赖 matrix-shared-webflux 和 matrix-shared-cloud-discovery-server

### 数据库迁移模块 (matrix-flyway)
- 管理数据库迁移脚本
- 使用 Flyway 进行数据库版本控制
- 依赖 PostgreSQL JDBC 驱动

## 6. 配置和脚本文件

### compose.yaml
- 定义了 PostgreSQL 服务的 Docker 配置
- 数据库名称: matrix_db
- 用户名: myuser
- 密码: secret
- 端口: 5432

### kill-port.sh
- 用于杀死占用指定端口的进程的脚本
- 默认端口: 9090
- 命令: `./kill-port.sh [port_number]`

### README-SPRING-CLOUD.md
- 列出了 Spring Cloud 版本与 Spring Boot 版本的对应关系

## 7. 数据库设计和迁移

### 数据库创建脚本
- 位于 `create_matrix_db.sql` 文件
- 使用 UTF8 编码支持中文
- 使用 zh_CN.UTF-8 本地化设置

### 数据库迁移脚本
- 位于 `matrix-flyway/src/main/resources/db/migration/` 目录
- 包括以下脚本：
  - `V20251114090000__create_database.sql`: 创建数据库（带中文支持）
  - `V20251114100001__init_auth_schema.sql`: 初始化认证模块表结构
  - `V20251114100002__init_user_schema.sql`: 初始化用户模块表结构

### 表结构说明
1. **认证模块表**：
   - `sys_user`: 用户信息表
   - `sys_role`: 角色信息表
   - `sys_permission`: 权限信息表
   - `sys_role_path`: 角色路径权限表
   - `sys_user_role`: 用户角色关联表
   - `sys_role_permission`: 角色权限关联表

2. **用户模块表**：
   - `matrix.user`: Matrix用户信息表

## 8. 开发和构建说明

### 构建命令
- 使用 Maven 构建项目: `mvn clean install`
- 跳过测试构建: `mvn clean install -DskipTests`
- 构建特定模块: `mvn clean install -pl module-name`
- 运行 Flyway 迁移: `mvn flyway:migrate -pl matrix-flyway`

### 项目依赖关系
- 所有模块都继承自根模块 matrix
- 通过 Maven BOM (Bill of Materials) 管理版本依赖
- 使用统一的依赖版本管理，避免版本冲突
- 各模块之间通过 gRPC 和 REST API 进行通信