--liquibase formatted sql

--changeset nikita:2025-10-19-create-subscription_plan
CREATE TABLE IF NOT EXISTS subscription_plan
(
    id              UUID         NOT NULL PRIMARY KEY,
    price_amount    NUMERIC(18,2) NOT NULL DEFAULT 0,
    duration_days   INT          NOT NULL,
    CONSTRAINT chk_subscription_plan_duration CHECK (duration_days > 0)
);

INSERT INTO subscription_plan (id, price_amount, duration_days)
VALUES
    (gen_random_uuid(), 999, 30),
    (gen_random_uuid(), 1999, 180),
    (gen_random_uuid(), 2999, 365);

ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS subscription_plan_id UUID NOT NULL;

ALTER TABLE subscriptions
    ADD CONSTRAINT fk_subscriptions_plan
        FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plan(id) ON DELETE RESTRICT;

ALTER TABLE subscriptions DROP CONSTRAINT IF EXISTS chk_subscriptions_plan;
ALTER TABLE subscriptions DROP COLUMN IF EXISTS plan;

--rollback DROP INDEX IF EXISTS ix_subscriptions_plan_id;
--rollback DROP TABLE IF EXISTS subscription_plan;