--liquibase formatted sql

--changeset nikita:2025-10-19-update-subscriptions
UPDATE subscriptions
SET status = 'EXPIRED'
WHERE status = 'CANCELED';

ALTER TABLE subscriptions DROP CONSTRAINT IF EXISTS chk_subscriptions_status;

ALTER TABLE subscriptions DROP COLUMN IF EXISTS canceled_at;
ALTER TABLE subscriptions DROP COLUMN IF EXISTS auto_renew;

ALTER TABLE subscriptions
    ADD CONSTRAINT chk_subscriptions_status
        CHECK (status IN ('PENDING','ACTIVE','EXPIRED'));

WITH premium AS (
    SELECT id FROM subscription_plan WHERE duration_days = 365
),
to_upsert(user_id) AS (
    VALUES
        ('c04bda93-74d2-4953-bc5c-f5abbe2594e4'::uuid),
        ('141c4751-17e6-4caf-89de-02b22e98f26f'::uuid),
        ('7a54f1ee-646b-4b75-b646-0420b3e49529'::uuid),
        ('46d699b1-6612-44ed-bf90-011ad5501c67'::uuid),
        ('27638aba-432f-4de6-ab01-c9bc8474bd7e'::uuid)
)
INSERT INTO subscriptions (id,user_id, subscription_plan_id, status, started_at, expires_at, price_amount, price_currency, ext_provider, ext_subscription_id)
SELECT
    tu.user_id                    AS id,
    tu.user_id,
    p.id                          AS subscription_plan_id,
    'ACTIVE'                      AS status,
    now()                         AS started_at,
    TIMESTAMPTZ '2030-12-31 23:59:59+00' AS expires_at,
    NULL, NULL, NULL, NULL
FROM to_upsert tu
CROSS JOIN premium p;
--rollback ALTER TABLE subscriptions DROP CONSTRAINT IF EXISTS chk_subscriptions_status;
--rollback ALTER TABLE subscriptions
--rollback     ADD CONSTRAINT chk_subscriptions_status
--rollback         CHECK (status IN ('PENDING','ACTIVE','CANCELED','EXPIRED'));
--rollback ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS auto_renew BOOLEAN NOT NULL DEFAULT false;
--rollback ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS canceled_at TIMESTAMPTZ NULL;