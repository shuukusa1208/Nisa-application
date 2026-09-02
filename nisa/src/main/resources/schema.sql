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
