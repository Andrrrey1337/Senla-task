TRUNCATE TABLE users CASCADE;

INSERT INTO users (id, username, password, role, is_active, balance)
VALUES (1, 'regularuser', 'hash123', 'USER', true, 100.00);

ALTER SEQUENCE users_id_seq RESTART WITH 51;