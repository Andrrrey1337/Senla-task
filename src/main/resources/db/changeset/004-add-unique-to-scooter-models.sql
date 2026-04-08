--liquibase formatted sql

--changeset admin:add-unique-scooter-models-name

ALTER TABLE scooter_models ADD UNIQUE (name);