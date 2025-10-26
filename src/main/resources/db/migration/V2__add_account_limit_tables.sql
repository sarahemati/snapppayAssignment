-- ===============================================
--  V2__add_account_limit_tables.sql
--  Adds daily & transaction limit configuration
--  and usage tracking tables.
-- ===============================================

-- 1️⃣ Account Limit Configuration
CREATE TABLE IF NOT EXISTS account_limit_config (
                                                    id BIGSERIAL PRIMARY KEY,
                                                    user_id BIGINT NOT NULL,
                                                    daily_limit DECIMAL(19,2) NOT NULL DEFAULT 10000,
    single_tx_limit DECIMAL(19,2) NOT NULL DEFAULT 5000,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_limit_config_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- 2️⃣ Account Limit Usage (daily tracking)
CREATE TABLE IF NOT EXISTS account_limit_usage (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   user_id BIGINT NOT NULL,
                                                   date DATE NOT NULL,
                                                   used_amount DECIMAL(19,2) NOT NULL DEFAULT 0,
    last_tx_id UUID,
    updated_at TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_limit_usage_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_limit_usage_user_date UNIQUE (user_id, date)
    );

-- 3️⃣ Optional Indexes
CREATE INDEX IF NOT EXISTS idx_limit_usage_user_date ON account_limit_usage(user_id, date);
CREATE INDEX IF NOT EXISTS idx_limit_config_user ON account_limit_config(user_id);
