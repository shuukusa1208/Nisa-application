CREATE TABLE IF NOT EXISTS assets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100),
    quantity BIGINT NOT NULL DEFAULT 0,
    acquisition BIGINT NOT NULL DEFAULT 0,
    current_value BIGINT NOT NULL DEFAULT 0,
    purchase_date DATE,
    frame VARCHAR(50) NOT NULL,
    memo VARCHAR(1000),
    PRIMARY KEY (id),
    CONSTRAINT fk_assets_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS nisa_quotas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    tsumitate_used BIGINT NOT NULL DEFAULT 0,
    growth_used BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_nisa_quotas_user (user_id),
    CONSTRAINT fk_nisa_quotas_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    expires_at DATETIME NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_password_reset_token (token),
    UNIQUE KEY uk_password_reset_user (user_id),
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users (id)
);
