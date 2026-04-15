--liquibase formatted sql

--changeset admin:add-held-balance-to-users
ALTER TABLE users ADD COLUMN held_balance DECIMAL(10, 2) DEFAULT 0.0;
