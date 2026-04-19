-- очистка таблицы перед тестом
TRUNCATE TABLE tariffs CASCADE;

INSERT INTO tariffs (id, name, description, price)
VALUES (1, 'Базовый', 'Описание базового тарифа', 50.00);

INSERT INTO tariffs (id, name, description, price)
VALUES (2, 'Премиум', 'Описание премиум тарифа', 150.00);

-- сдвигаем сиквенс чтобы избежать конфликтов при запросах
ALTER SEQUENCE tariffs_id_seq RESTART WITH 51;