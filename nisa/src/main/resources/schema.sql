-- users テーブル（参照される側を最優先で作成）
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- assets テーブル
CREATE TABLE IF NOT EXISTS assets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100),
    quantity BIGINT NOT NULL DEFAULT 0,
    acquisition BIGINT NOT NULL DEFAULT 0,
    current_value BIGINT NOT NULL DEFAULT 0,
    purchase_date DATE,
    frame VARCHAR(50) NOT NULL,
    memo VARCHAR(1000),
    CONSTRAINT fk_assets_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- nisa_quotas テーブル
CREATE TABLE IF NOT EXISTS nisa_quotas (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tsumitate_used BIGINT NOT NULL DEFAULT 0,
    growth_used BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_nisa_quotas_user UNIQUE (user_id),
    CONSTRAINT fk_nisa_quotas_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- password_reset_tokens テーブル
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_password_reset_token UNIQUE (token),
    CONSTRAINT uk_password_reset_user UNIQUE (user_id),
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users (id)
);