-- 确保在 matrix 数据库模式下创建表
CREATE TABLE IF NOT EXISTS matrix.matrix_register_setting (
    id BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL, -- 客户端名称（默认语言）
    client_name_en_us VARCHAR(255), -- 客户端名称（英语-美国）
    client_name_en_gb VARCHAR(255), -- 客户端名称（英语-英国）
    client_name_fr VARCHAR(255), -- 客户端名称（法语）
    tos_uri VARCHAR(500), -- 服务条款链接
    tos_uri_fr VARCHAR(500), -- 服务条款链接（法语）
    policy_uri VARCHAR(500), -- 隐私政策链接
    policy_uri_fr VARCHAR(500), -- 隐私政策链接（法语）
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted_at TIMESTAMP DEFAULT NULL -- 软删除时间戳
);

-- 为时间戳字段创建索引
CREATE INDEX IF NOT EXISTS idx_matrix_register_setting_created_time ON matrix.matrix_register_setting (created_time);
CREATE INDEX IF NOT EXISTS idx_matrix_register_setting_updated_time ON matrix.matrix_register_setting (updated_time);