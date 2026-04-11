--liquibase formatted sql

--changeset admin:create-users-sequence-1
CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-users-2
CREATE TABLE users(
    id BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    username VARCHAR(50) UNIQUE NOT NULL ,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    balance DECIMAL(10,2) DEFAULT 0.0
);

--changeset admin:create-scooter-models-3
CREATE SEQUENCE scooter_models_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-scooter-models-4
CREATE TABLE scooter_models(
    id BIGINT PRIMARY KEY DEFAULT nextval('scooter_models_id_seq'),
    name VARCHAR(50) NOT NULL,
    price_per_minute DECIMAL(10,2) NOT NULL,
    max_speed INTEGER
);

--changeset admin:create-rental-points-seq-5

CREATE SEQUENCE rental_points_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-rental-points-6

CREATE TABLE rental_points(
    id BIGINT PRIMARY KEY DEFAULT nextval('rental_points_id_seq'),
    name VARCHAR(50) NOT NULL,
    parent_id BIGINT REFERENCES rental_points(id) ON DELETE SET NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(10,8) NOT NULL
);

--changeset admin:create-scooter-sequence-7

CREATE SEQUENCE scooters_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-scooter-8
CREATE TABLE scooters(
    id BIGINT PRIMARY KEY DEFAULT nextval('scooters_id_seq'),
    serial_number VARCHAR(20) NOT NULL UNIQUE,
    model_id BIGINT REFERENCES scooter_models(id),
    rental_point_id BIGINT REFERENCES rental_points(id) ON DELETE SET NULL,
    battery_level INTEGER NOT NULL DEFAULT 0,
    latitude DECIMAL(10,8),
    longitude DECIMAL(10,8),
    status VARCHAR(40) NOT NULL
);

--changeset admin:create-tariffs-sequence-9

CREATE SEQUENCE tariffs_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-tariffs-10

CREATE TABLE tariffs(
    id BIGINT PRIMARY KEY DEFAULT nextval('tariffs_id_seq'),
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    price DECIMAL(10, 2) NOT NULL DEFAULT 0.0
);

--changeset admin:create-rentals-seq-11

CREATE SEQUENCE rentals_id_seq START WITH 1 INCREMENT BY 50;

--changeset admin:create-rentals-12

CREATE TABLE rentals(
    id BIGINT PRIMARY KEY DEFAULT nextval('rentals_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scooter_id BIGINT NOT NULL REFERENCES scooters(id) ON DELETE CASCADE,
    tariff_id BIGINT NOT NULL REFERENCES tariffs(id) ON DELETE RESTRICT ,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    start_lat DECIMAL(10,8) NOT NULL,
    start_lon DECIMAL(10,8) NOT NULL,
    end_lat DECIMAL(10,8),
    end_lon DECIMAL(10,8),
    price DECIMAL(10,2),
    is_active BOOLEAN NOT NULL
);
