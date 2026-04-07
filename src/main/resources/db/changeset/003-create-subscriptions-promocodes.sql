--liquibase formatted sql

--changeset admin:create-promo-codes-1
CREATE SEQUENCE promo_codes_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE promo_codes(
    id BIGINT PRIMARY KEY DEFAULT nextval('promo_codes_id_seq'),
    code VARCHAR(20) NOT NULL UNIQUE,
    discount INTEGER NOT NULL,
    end_date TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

--changeset admin:create-subscriptions-2

CREATE SEQUENCE subscriptions_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE subscriptions(
    id BIGINT PRIMARY KEY DEFAULT nextval('subscriptions_id_seq'),
    name VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    duration_days INTEGER NOT NULL,
    is_free_start BOOLEAN NOT NULL DEFAULT FALSE,
    include_minutes INTEGER NOT NULL DEFAULT 0 -- если 0, то только фри старт
);

--changeset admin:create-user-subscriptions-3

CREATE SEQUENCE user_subscriptions_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE user_subscriptions(
    id BIGINT PRIMARY KEY DEFAULT nextval('user_subscriptions_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subscription_id BIGINT NOT NULL REFERENCES subscriptions(id) ON DELETE CASCADE,
    end_date TIMESTAMP NOT NULL,
    remaining_minutes INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

--changeset admin:update-rentals-add-promo-code-4

ALTER TABLE rentals ADD COLUMN promo_code_id BIGINT REFERENCES promo_codes(id) ON DELETE SET NULL;