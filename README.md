# 项目环境依赖列表

## 核心环境配置
- **Java 版本**: 21 (主项目) / 24 (matrix-flyway模块)
- **Maven 编译插件**: 使用版本 3.14.0

## Spring Boot 生态系统
- **Spring Boot 版本**: 3.5.4
- **Spring Cloud 版本**: 2025.0.0
- **Spring Cloud Alibaba 版本**: 2023.0.3.3
- **Spring Security 版本**: 6.5.2
- **Spring Framework 版本**: 6.2.9
- **Spring Kafka 版本**: 3.3.8

## 数据库相关
- **PostgreSQL R2DBC 驱动**: 1.0.7.RELEASE
- **PostgreSQL JDBC 驱动** (在 matrix-flyway 模块中): 42.7.7
- **R2DBC 连接池**: 使用 Spring Boot 管理的版本

## 工具和库
- **Lombok 版本**: 1.18.38
- **Flyway 版本**:
    - Core: 11.10.1 (主项目)
    - Database PostgreSQL: 11.10.1 (主项目)
    - matrix-flyway 模块中使用: 11.11.0

## 构建和测试工具
- **Maven Surefire 插件**: 3.5.3
- **Maven Compiler 插件**: 3.14.0
- **Maven Resources 插件**: 3.3.1

## 其他重要依赖
- **Spring WebFlux**: 使用 Spring Boot 管理的版本
- **Spring Data R2DBC**: 使用 Spring Boot 管理的版本

该项目是一个基于 Spring Boot 3.5.4 和 Java 21 构建的微服务架构系统，使用 PostgreSQL 作为数据库，并通过 R2DBC 实现响应式数据访问。Lombok 用于简化 Java 代码，Flyway 用于数据库迁移。
