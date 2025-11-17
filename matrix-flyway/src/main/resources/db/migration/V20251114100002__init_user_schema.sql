-- 创建用户模块相关数据库表
-- 包含 Matrix 用户信息表

-- 创建 matrix schema (如果不存在)
CREATE SCHEMA IF NOT EXISTS matrix;

-- 创建 matrix.user 表
CREATE TABLE IF NOT EXISTS matrix."user" (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE, -- Matrix用户ID，格式为@username:domain
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    email VARCHAR(255),
    display_name VARCHAR(255),
    avatar_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP WITHOUT TIME ZONE
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_matrix_user_user_id ON matrix."user"(user_id);
CREATE INDEX IF NOT EXISTS idx_matrix_user_username ON matrix."user"(username);
CREATE INDEX IF NOT EXISTS idx_matrix_user_email ON matrix."user"(email);

-- 插入默认用户示例
INSERT INTO matrix."user" (user_id, username, password, email, display_name, is_active, created_at)
SELECT '@admin:matrix.org', 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@matrix.org', 'Admin User', TRUE, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix."user" WHERE user_id = '@admin:matrix.org');