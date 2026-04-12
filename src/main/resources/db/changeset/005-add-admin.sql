INSERT INTO users (id, username, password, role, is_active, balance)
VALUES (
           nextval('users_id_seq'),
           'admin',
           '$2a$12$3ZZPGNzlgWmMnMyBvf4lG.tfV7LzcR3Va6vuxF4WkV.oVoHf7P69O',
           'ADMIN',
           true,
           0.00
       );