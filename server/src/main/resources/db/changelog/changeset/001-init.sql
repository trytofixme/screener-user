--liquibase formatted sql

--changeset screener:001
CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    tg_user_id BIGINT NOT NULL UNIQUE,
    chat_id BIGINT NOT NULL,
    username VARCHAR(64),
    first_name VARCHAR(128),
    last_name  VARCHAR(128),
    subscription_until TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_settings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    long_period_min  INT,
    short_period_min INT,
    dump_period_min  INT,
    long_percent  NUMERIC(7,3),
    short_percent NUMERIC(7,3),
    dump_percent  NUMERIC(7,3),
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE exchange_pref (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    bybit_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    moex_enabled  BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_state (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    state VARCHAR(64) NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
--rollback DROP TABLE user_state; DROP TABLE exchange_pref; DROP TABLE user_settings; DROP TABLE app_user;