--liquibase formatted sql

--changeset screener:001
CREATE TABLE IF NOT EXISTS app_user (
    id UUID PRIMARY KEY,
    tg_user_id BIGINT NOT NULL UNIQUE,
    chat_id BIGINT NOT NULL,
    username VARCHAR(64),
    first_name VARCHAR(128),
    last_name  VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS rsi_settings (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    short_time_frame VARCHAR(16),
    long_time_frame VARCHAR(16),
    short_rsi NUMERIC,
    long_rsi NUMERIC,
    updated_at TIMESTAMPTZ NOT NULL

    CONSTRAINT chk_short_time_frame
            CHECK (short_time_frame IN ('NONE','FIFTEEN_MINUTES','ONE_HOUR','FOUR_HOURS','ONE_DAY')),

    CONSTRAINT chk_long_time_frame
            CHECK (long_time_frame IN ('NONE','FIFTEEN_MINUTES','ONE_HOUR','FOUR_HOURS','ONE_DAY'))
);

CREATE TABLE IF NOT EXISTS user_state (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    state VARCHAR(64) NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL

    CONSTRAINT chk_user_state
            CHECK (state IN ('NONE','WAIT_TIME_FRAME_LONG', 'WAIT_TIME_FRAME_SHORT', 'WAIT_RSI_LONG', 'WAIT_RSI_SHORT'))
);
--rollback DROP TABLE app_user; DROP TABLE rsi_settings; DROP TABLE user_state;