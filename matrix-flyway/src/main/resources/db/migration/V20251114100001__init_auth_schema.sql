-- 创建认证模块相关数据库表
-- 包含用户、角色、权限、路径权限、用户角色关联和角色权限关联表

-- 创建 sys_user 表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_role 表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_permission 表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_role_path 表
CREATE TABLE IF NOT EXISTS sys_role_path (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    path_pattern VARCHAR(255) NOT NULL,
    http_method VARCHAR(10) DEFAULT 'GET',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建 sys_user_role 关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
);

-- 创建 sys_role_permission 关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id) REFERENCES sys_permission(id) ON DELETE CASCADE
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_sys_user_username ON sys_user(username);
CREATE INDEX IF NOT EXISTS idx_sys_role_name ON sys_role(name);
CREATE INDEX IF NOT EXISTS idx_sys_permission_code ON sys_permission(code);
CREATE INDEX IF NOT EXISTS idx_sys_role_path_role_name ON sys_role_path(role_name);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_user_id ON sys_user_role(user_id);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_role_id ON sys_user_role(role_id);

-- 插入默认角色
INSERT INTO sys_role (name, description, created_at, updated_at) 
SELECT 'ADMIN', '系统管理员角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP 
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE name = 'ADMIN');

INSERT INTO sys_role (name, description, created_at, updated_at) 
SELECT 'USER', '普通用户角色', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP 
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE name = 'USER');

-- 插入默认权限
INSERT INTO sys_permission (code, description, created_at, updated_at)
SELECT 'USER_READ', '用户读取权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE code = 'USER_READ');

INSERT INTO sys_permission (code, description, created_at, updated_at)
SELECT 'USER_WRITE', '用户写入权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE code = 'USER_WRITE');

INSERT INTO sys_permission (code, description, created_at, updated_at)
SELECT 'ADMIN_ACCESS', '管理员访问权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE code = 'ADMIN_ACCESS');

-- 插入默认用户 (用户名: admin, 密码需要进行BCrypt加密)
INSERT INTO sys_user (username, password, enabled, created_at, updated_at)
SELECT 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE username = 'admin');