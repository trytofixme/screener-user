--liquibase formatted sql

--changeset screener:002 sqlStripComments:true
CREATE TABLE IF NOT EXISTS subscriptions
(
    id                     UUID        NOT NULL PRIMARY KEY,
    user_id                UUID        NOT NULL UNIQUE REFERENCES app_user(id) ON DELETE CASCADE,
    plan                   TEXT        NOT NULL,
    status                 TEXT        NOT NULL,
    started_at             TIMESTAMPTZ NOT NULL,
    expires_at             TIMESTAMPTZ NULL,
    canceled_at            TIMESTAMPTZ NULL,
    auto_renew             BOOLEAN     NOT NULL DEFAULT false,
    price_amount           NUMERIC(18,2) NULL,
    price_currency         VARCHAR(16)  NULL,
    ext_provider           VARCHAR(32) NULL,
    ext_subscription_id    VARCHAR(128) NULL,
    version                INT     NOT NULL DEFAULT 0,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at             TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT chk_subscriptions_plan
        CHECK (plan IN ('FREE','PREMIUM','SUPER')),

    CONSTRAINT chk_subscriptions_status
        CHECK (status IN ('PENDING','ACTIVE','CANCELED','EXPIRED'))
);

CREATE INDEX IF NOT EXISTS ix_subscriptions_user_id ON subscriptions (user_id);
CREATE INDEX IF NOT EXISTS ix_subscriptions_status ON subscriptions (status);
CREATE INDEX IF NOT EXISTS ix_subscriptions_user_status ON subscriptions (user_id, status);

--rollback DROP INDEX IF EXISTS ix_subscriptions_user_status;
--rollback DROP INDEX IF EXISTS ix_subscriptions_status;
--rollback DROP INDEX IF EXISTS ix_subscriptions_user_id;
--rollback DROP TABLE IF EXISTS subscriptions;