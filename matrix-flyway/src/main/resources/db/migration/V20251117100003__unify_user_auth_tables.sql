-- 统一用户认证表结构，将认证功能集成到 matrix.user 表中
-- 同时清理旧的不合理的认证表结构

-- 添加认证相关字段到 matrix.user 表
ALTER TABLE matrix."user" 
ADD COLUMN IF NOT EXISTS enabled BOOLEAN DEFAULT TRUE,
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP;

-- 创建 sys_role 表（在 matrix 模式中）
CREATE TABLE IF NOT EXISTS matrix.sys_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,  -- 角色名称，例如 "ADMIN", "USER"
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_permission 表（在 matrix 模式中）
CREATE TABLE IF NOT EXISTS matrix.sys_permission (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,  -- 权限代码，例如 "USER_READ", "USER_WRITE"
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_role_path 表（在 matrix 模式中）
CREATE TABLE IF NOT EXISTS matrix.sys_role_path (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    path_pattern VARCHAR(255) NOT NULL,
    http_method VARCHAR(10) DEFAULT 'GET',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_user_role 关联表（在 matrix 模式中）
CREATE TABLE IF NOT EXISTS matrix.sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES matrix."user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES matrix.sys_role(id) ON DELETE CASCADE
);

-- 创建 sys_role_permission 关联表（在 matrix 模式中）
CREATE TABLE IF NOT EXISTS matrix.sys_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES matrix.sys_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES matrix.sys_permission(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_matrix_user_username ON matrix."user"(username);
CREATE INDEX IF NOT EXISTS idx_matrix_user_email ON matrix."user"(email);
CREATE INDEX IF NOT EXISTS idx_matrix_user_enabled ON matrix."user"(enabled);
CREATE INDEX IF NOT EXISTS idx_sys_role_name ON matrix.sys_role(name);
CREATE INDEX IF NOT EXISTS idx_sys_permission_code ON matrix.sys_permission(code);
CREATE INDEX IF NOT EXISTS idx_sys_role_path_role_name ON matrix.sys_role_path(role_name);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_user_id ON matrix.sys_user_role(user_id);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_role_id ON matrix.sys_user_role(role_id);

-- 插入默认角色
INSERT INTO matrix.sys_role (name, description, created_at, updated_at)
SELECT 'ADMIN', '系统管理员角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix.sys_role WHERE name = 'ADMIN');

INSERT INTO matrix.sys_role (name, description, created_at, updated_at)
SELECT 'USER', '普通用户角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix.sys_role WHERE name = 'USER');

-- 插入默认权限
INSERT INTO matrix.sys_permission (code, description, created_at, updated_at)
SELECT 'USER_READ', '用户读取权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix.sys_permission WHERE code = 'USER_READ');

INSERT INTO matrix.sys_permission (code, description, created_at, updated_at)
SELECT 'USER_WRITE', '用户写入权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix.sys_permission WHERE code = 'USER_WRITE');

INSERT INTO matrix.sys_permission (code, description, created_at, updated_at)
SELECT 'ADMIN_ACCESS', '管理员访问权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM matrix.sys_permission WHERE code = 'ADMIN_ACCESS');