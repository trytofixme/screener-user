--liquibase formatted sql

--changeset nikita:2025-09-30-add-dump-column
ALTER TABLE rsi_settings ADD COLUMN short_dump NUMERIC;
ALTER TABLE rsi_settings ADD COLUMN long_dump NUMERIC;

--changeset nikita:2025-09-30-set-dump-default
UPDATE rsi_settings
SET short_dump = 1;

UPDATE rsi_settings
SET long_dump = 1;