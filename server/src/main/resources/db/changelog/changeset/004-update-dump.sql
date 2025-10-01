--liquibase formatted sql

--changeset nikita:2025-10-01-update-dump-default
UPDATE rsi_settings
SET short_dump = 2;

UPDATE rsi_settings
SET long_dump = 2;