--liquibase formatted sql

--changeset aglushkovsky:1
INSERT INTO "user" (id, login, password_hash, email, phone_number, role, total_rating)
VALUES (1, 'test_user',
        '$2a$12$sGu145CigoPn4HWjLOndX.QenSaUBRZreuex2fdo3wtUriQ9dBwie', -- qwerty12345
        NULL,
        NULL,
        'USER',
        0.0),
       (2, 'test_admin',
        '$2a$12$pAMuVi4R48STh59Sh6OXP.d95htFMnq6XH.f5zDyepepB/REO.MsC', -- password
        NULL,
        NULL,
        'ADMIN',
        0.0);

--changeset aglushkovsky:2
-- TODO Подумать, действительно ли это надо здесь?
SELECT SETVAL('user_id_seq', (SELECT MAX(id) FROM public.user));

--changeset aglushkovsky:3
WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Орёл', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

--changeset aglushkovsky:4
WITH ancestors AS (SELECT ancestor_locality_id, depth
                   FROM locality_parts_relation lpr
                            JOIN locality l ON l.id = lpr.descendant_locality_id
                   WHERE name = 'Орёл'
                     AND type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('Советский', 'DISTRICT'),
                                                  ('Железнодорожный', 'DISTRICT'),
                                                  ('Заводской', 'DISTRICT'),
                                                  ('Северный', 'DISTRICT')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:5
WITH ancestors AS (SELECT lpr.ancestor_locality_id, lpr.depth
                   FROM locality_parts_relation lpr
                            JOIN locality descendant
                                 ON descendant.id = lpr.descendant_locality_id
                            JOIN locality_parts_relation city_relation
                                 ON city_relation.descendant_locality_id = descendant.id
                            JOIN locality city
                                 ON city.id = city_relation.ancestor_locality_id
                   WHERE descendant.name = 'Советский'
                     AND descendant.type = 'DISTRICT'
                     AND city.name = 'Орёл'
                     AND city.type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('Полесская', 'STREET')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:6
WITH ancestors AS (SELECT lpr.ancestor_locality_id, lpr.depth, city.name
                   FROM locality_parts_relation lpr
                            JOIN locality descendant
                                 ON descendant.id = lpr.descendant_locality_id
                            JOIN locality_parts_relation city_relation
                                 ON city_relation.descendant_locality_id = descendant.id
                            JOIN locality city
                                 ON city.id = city_relation.ancestor_locality_id
                   WHERE descendant.name = 'Полесская'
                     AND descendant.type = 'STREET'
                     AND city.name = 'Орёл'
                     AND city.type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('6', 'HOUSE_NUMBER')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:7
WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Мценск', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

--changeset aglushkovsky:8
WITH ancestors AS (SELECT ancestor_locality_id, depth
                   FROM locality_parts_relation lpr
                            JOIN locality l ON l.id = lpr.descendant_locality_id
                   WHERE name = 'Мценск'
                     AND type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('Мира', 'STREET'),
                                                  ('Тургенева', 'STREET'),
                                                  ('Ленина', 'STREET'),
                                                  ('Гагарина', 'STREET')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:9
WITH ancestors AS (SELECT lpr.ancestor_locality_id, lpr.depth, city.name
                   FROM locality_parts_relation lpr
                            JOIN locality descendant
                                 ON descendant.id = lpr.descendant_locality_id
                            JOIN locality_parts_relation city_relation
                                 ON city_relation.descendant_locality_id = descendant.id
                            JOIN locality city
                                 ON city.id = city_relation.ancestor_locality_id
                   WHERE descendant.name = 'Мира'
                     AND descendant.type = 'STREET'
                     AND city.name = 'Мценск'
                     AND city.type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('1', 'HOUSE_NUMBER'),
                                                  ('2', 'HOUSE_NUMBER'),
                                                  ('3', 'HOUSE_NUMBER'),
                                                  ('4', 'HOUSE_NUMBER'),
                                                  ('5', 'HOUSE_NUMBER')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:10
WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Брянск', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

--changeset aglushkovsky:11
WITH ancestors AS (SELECT ancestor_locality_id, depth
                   FROM locality_parts_relation lpr
                            JOIN locality l ON l.id = lpr.descendant_locality_id
                   WHERE name = 'Брянск'
                     AND type = 'CITY'),
     new_descendant_locality AS (
         INSERT INTO locality (name, type) VALUES ('Бежицкий', 'DISTRICT'),
                                                  ('Володарский', 'DISTRICT'),
                                                  ('Советский', 'DISTRICT'),
                                                  ('Фокинский', 'DISTRICT')
             RETURNING id)
INSERT
INTO locality_parts_relation (ancestor_locality_id, descendant_locality_id, depth)
SELECT a.ancestor_locality_id, ndl.id, a.depth + 1
FROM ancestors a
         CROSS JOIN new_descendant_locality ndl
UNION ALL
SELECT new_descendant_locality.id, new_descendant_locality.id, 0
FROM new_descendant_locality;

--changeset aglushkovsky:12
INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, is_promoted)
VALUES ('Macbook Pro M4 16/512 новый запечатанный из ОАЭ',
        15000000,
        'Зaпaкованныe и пoлнocтью нoвыe.' ||
        'Очень мощныe и подxoдят под любые зaдачи.' ||
        'Пoдберeм Baм нужную кoмплeктaцию. На мecтe можeм егo прoверить.',
        (SELECT lpr.ancestor_locality_id
         FROM locality_parts_relation lpr
                  JOIN locality descendant
                       ON descendant.id = lpr.descendant_locality_id
                  JOIN locality_parts_relation street_relation
                       ON street_relation.descendant_locality_id = descendant.id
                  JOIN locality street
                       ON street.id = street_relation.ancestor_locality_id
                  JOIN locality_parts_relation city_relation
                       ON city_relation.descendant_locality_id = descendant.id
                  JOIN locality city
                       ON city.id = city_relation.ancestor_locality_id
         WHERE descendant.name = '6'
           AND descendant.type = 'HOUSE_NUMBER'
           AND street.name = 'Полесская'
           AND street.type = 'STREET'
           AND city.name = 'Орёл'
           AND city.type = 'CITY'
           AND lpr.depth = 0),
        (SELECT id FROM "user" WHERE login = 'test_user'),
        NOW(),
        TRUE);

--changeset aglushkovsky:13
INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, is_promoted)
VALUES ('Xiaomi Redmi Note 13 256 ГБ',
        1000000,
        'В хорошем состоянии',
        (SELECT lpr.ancestor_locality_id
         FROM locality_parts_relation lpr
                  JOIN locality descendant
                       ON descendant.id = lpr.descendant_locality_id
                  JOIN locality_parts_relation street_relation
                       ON street_relation.descendant_locality_id = descendant.id
                  JOIN locality street
                       ON street.id = street_relation.ancestor_locality_id
                  JOIN locality_parts_relation city_relation
                       ON city_relation.descendant_locality_id = descendant.id
                  JOIN locality city
                       ON city.id = city_relation.ancestor_locality_id
         WHERE descendant.name = '1'
           AND descendant.type = 'HOUSE_NUMBER'
           AND street.name = 'Мира'
           AND street.type = 'STREET'
           AND city.name = 'Мценск'
           AND city.type = 'CITY'
           AND lpr.depth = 0),
        (SELECT id FROM "user" WHERE login = 'test_user'),
        NOW(),
        FALSE);

--changeset aglushkovsky:14
INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, is_promoted)
VALUES ('телефон xiaomi A3x',
        250000,
        'пользовались мало. коробку потеряли',
        (SELECT lpr.ancestor_locality_id
         FROM locality_parts_relation lpr
                  JOIN locality descendant
                       ON descendant.id = lpr.descendant_locality_id
                  JOIN locality_parts_relation city_relation
                       ON city_relation.descendant_locality_id = descendant.id
                  JOIN locality city
                       ON city.id = city_relation.ancestor_locality_id
         WHERE descendant.name = 'Тургенева'
           AND descendant.type = 'STREET'
           AND city.name = 'Мценск'
           AND city.type = 'CITY'
           AND lpr.depth = 0),
        (SELECT id FROM "user" WHERE login = 'test_user'),
        NOW(),
        FALSE);
