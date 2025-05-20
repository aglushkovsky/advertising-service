--liquibase formatted sql

--changeset aglushkovsky:insert_table_locality_locality_parts_relation
WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Орёл', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

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

WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Мценск', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

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

WITH root_locality AS (
    INSERT INTO locality (name, type) VALUES ('Брянск', 'CITY') RETURNING id)
INSERT
INTO locality_parts_relation(ancestor_locality_id, descendant_locality_id, depth)
SELECT root_locality.id, root_locality.id, 0
FROM root_locality;

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