create database userpassport;

comment on database userpassport is '用户库';

-----------------------------------------------------------------------
--用户信息表
CREATE TABLE IF NOT EXISTS user_info
(
    userid     BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    nickname   VARCHAR(50)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_user_info_email ON user_info (email);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_info_phone ON user_info (phone);
CREATE INDEX IF NOT EXISTS idx_user_info_userid ON user_info (userid);

COMMENT ON TABLE user_info IS '用户信息表';
COMMENT ON COLUMN user_info.userid IS '用户ID，自增主键（BIGSERIAL类型，对应Java的long）';
COMMENT ON COLUMN user_info.email IS '邮箱地址';
COMMENT ON COLUMN user_info.phone IS '手机号码';
COMMENT ON COLUMN user_info.nickname IS '用户昵称';
COMMENT ON COLUMN user_info.password IS '密码（建议加密存储）';
COMMENT ON COLUMN user_info.created_at IS '创建时间';

-----------------------------------------------------------------------
-- 登录历史表（基于JWT验证机制）
CREATE TABLE IF NOT EXISTS login_history
(
    id             BIGSERIAL PRIMARY KEY,
    userid         BIGINT NOT NULL,
    login_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    token          VARCHAR(2048) NOT NULL, -- JWT令牌
    token_id       VARCHAR(255) NOT NULL, -- 令牌唯一标识
    token_expire   TIMESTAMP NOT NULL, -- 令牌过期时间
    login_ip       VARCHAR(50), -- 登录IP地址
    login_device   VARCHAR(255), -- 登录设备信息
    status         VARCHAR(20) NOT NULL, -- 登录状态（成功/失败）
    error_msg      TEXT, -- 错误信息（如果登录失败）
    token_revoked  BOOLEAN DEFAULT FALSE, -- 令牌是否被撤销
    FOREIGN KEY (userid) REFERENCES user_info (userid)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_login_history_userid ON login_history (userid);
CREATE INDEX IF NOT EXISTS idx_login_history_login_time ON login_history (login_time);
CREATE INDEX IF NOT EXISTS idx_login_history_token_id ON login_history (token_id);
CREATE INDEX IF NOT EXISTS idx_login_history_token_expire ON login_history (token_expire);

-- 添加表和字段注释
COMMENT ON TABLE login_history IS '用户登录历史表（基于JWT验证机制）';
COMMENT ON COLUMN login_history.id IS '记录ID，自增主键';
COMMENT ON COLUMN login_history.userid IS '用户ID，关联user_info表（BIGINT类型，对应Java的long）';
COMMENT ON COLUMN login_history.login_time IS '登录时间';
COMMENT ON COLUMN login_history.token IS 'JWT令牌';
COMMENT ON COLUMN login_history.token_id IS '令牌唯一标识（用于令牌管理）';
COMMENT ON COLUMN login_history.token_expire IS '令牌过期时间';
COMMENT ON COLUMN login_history.login_ip IS '登录IP地址';
COMMENT ON COLUMN login_history.login_device IS '登录设备信息';
COMMENT ON COLUMN login_history.status IS '登录状态（成功/失败）';
COMMENT ON COLUMN login_history.error_msg IS '错误信息（如果登录失败）';
COMMENT ON COLUMN login_history.token_revoked IS '令牌是否被撤销（用于登出或令牌失效管理）';

-----------------------------------------------------------------------
-- 用户身份表
CREATE TABLE IF NOT EXISTS user_identity
(
    id             BIGSERIAL PRIMARY KEY,
    userid         BIGINT NOT NULL,
    identity_type  VARCHAR(50) NOT NULL, -- 身份类型（如：管理员、普通用户、VIP等）
    identity_value VARCHAR(255) NOT NULL, -- 身份值（如：ADMIN、USER、VIP等）
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- 身份状态（ACTIVE/INACTIVE）
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userid) REFERENCES user_info (userid)
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_identity_userid ON user_identity (userid);
CREATE INDEX IF NOT EXISTS idx_user_identity_type ON user_identity (identity_type);
CREATE INDEX IF NOT EXISTS idx_user_identity_status ON user_identity (status);

-- 添加表和字段注释
COMMENT ON TABLE user_identity IS '用户身份表，支持用户拥有多种身份类型';
COMMENT ON COLUMN user_identity.id IS '记录ID，自增主键';
COMMENT ON COLUMN user_identity.userid IS '用户ID，关联user_info表（BIGINT类型，对应Java的long）';
COMMENT ON COLUMN user_identity.identity_type IS '身份类型（如：管理员、普通用户、VIP等）';
COMMENT ON COLUMN user_identity.identity_value IS '身份值（如：ADMIN、USER、VIP等）';
COMMENT ON COLUMN user_identity.status IS '身份状态（ACTIVE/INACTIVE）';
COMMENT ON COLUMN user_identity.created_at IS '创建时间';
COMMENT ON COLUMN user_identity.updated_at IS '更新时间';

-----------------------------------------------------------------------
-- 身份定义表（存储所有可能的身份类型）
CREATE TABLE IF NOT EXISTS identity_definition
(
    id          BIGSERIAL PRIMARY KEY,
    code        VARCHAR(50) NOT NULL UNIQUE, -- 身份唯一标识（如：admin、user）
    name        VARCHAR(100) NOT NULL, -- 身份名称（如：管理员、用户）
    description TEXT, -- 身份描述
    status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- 状态（ACTIVE/INACTIVE）
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_identity_definition_code ON identity_definition (code);
CREATE INDEX IF NOT EXISTS idx_identity_definition_status ON identity_definition (status);

-- 添加表和字段注释
COMMENT ON TABLE identity_definition IS '身份定义表，存储所有可能的身份类型';
COMMENT ON COLUMN identity_definition.id IS '记录ID，自增主键';
COMMENT ON COLUMN identity_definition.code IS '身份唯一标识（如：admin、user）';
COMMENT ON COLUMN identity_definition.name IS '身份名称（如：管理员、用户）';
COMMENT ON COLUMN identity_definition.description IS '身份描述';
COMMENT ON COLUMN identity_definition.status IS '状态（ACTIVE/INACTIVE）';
COMMENT ON COLUMN identity_definition.created_at IS '创建时间';
COMMENT ON COLUMN identity_definition.updated_at IS '更新时间';

-- 插入默认身份数据
INSERT INTO identity_definition (code, name, description) VALUES
('admin', '管理员', '系统管理员，拥有所有权限'),
('user', '用户', '普通用户，拥有基础权限')
ON CONFLICT (code) DO NOTHING; -- 避免重复插入

-----------------------------------------------------------------------
-- 用户操作日志表
CREATE TABLE IF NOT EXISTS user_operation_log
(
    log_id       BIGSERIAL PRIMARY KEY,
    operator_id  BIGINT, -- 操作人ID，关联user_info表
    target_user_id BIGINT NOT NULL, -- 被操作人ID，关联user_info表
    operation_type VARCHAR(50) NOT NULL, -- 操作类型（如：UPDATE_USER_INFO、CHANGE_STATUS等）
    operation_detail TEXT, -- 操作详情（JSON格式存储）
    old_value    TEXT, -- 旧值（JSON格式存储）
    new_value    TEXT, -- 新值（JSON格式存储）
    ip_address   VARCHAR(50), -- 操作IP地址
    user_agent   VARCHAR(255), -- 操作设备信息
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_user_operation_log_operator_id ON user_operation_log (operator_id);
CREATE INDEX IF NOT EXISTS idx_user_operation_log_target_user_id ON user_operation_log (target_user_id);
CREATE INDEX IF NOT EXISTS idx_user_operation_log_operation_type ON user_operation_log (operation_type);
CREATE INDEX IF NOT EXISTS idx_user_operation_log_created_at ON user_operation_log (created_at);

-- 添加表和字段注释
COMMENT ON TABLE user_operation_log IS '用户操作日志表，记录用户信息和状态的修改操作';
COMMENT ON COLUMN user_operation_log.log_id IS '日志ID，自增主键';
COMMENT ON COLUMN user_operation_log.operator_id IS '操作人ID，关联user_info表（BIGINT类型，对应Java的long），如果是用户自己操作则与target_user_id相同';
COMMENT ON COLUMN user_operation_log.target_user_id IS '被操作人ID，关联user_info表（BIGINT类型，对应Java的long）';
COMMENT ON COLUMN user_operation_log.operation_type IS '操作类型（如：UPDATE_USER_INFO、CHANGE_STATUS等）';
COMMENT ON COLUMN user_operation_log.operation_detail IS '操作详情（JSON格式存储）';
COMMENT ON COLUMN user_operation_log.old_value IS '旧值（JSON格式存储）';
COMMENT ON COLUMN user_operation_log.new_value IS '新值（JSON格式存储）';
COMMENT ON COLUMN user_operation_log.ip_address IS '操作IP地址';
COMMENT ON COLUMN user_operation_log.user_agent IS '操作设备信息';
COMMENT ON COLUMN user_operation_log.created_at IS '操作时间';