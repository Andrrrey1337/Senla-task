--liquibase formatted sql

--changeset admin:add-mileage-1
ALTER TABLE scooters ADD COLUMN mileage DECIMAL(10, 2) DEFAULT 0.0;

--changeset admin:add-distance-2
ALTER TABLE rentals ADD COLUMN distance DECIMAL(10, 2)