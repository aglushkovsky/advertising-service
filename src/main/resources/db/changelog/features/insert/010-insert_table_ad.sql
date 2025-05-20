--liquibase formatted sql

--changeset aglushkovsky:insert_table_ad
INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, status, is_promoted)
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
        'ACTIVE',
        TRUE);

INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, status, is_promoted)
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
        'ACTIVE',
        FALSE);

INSERT
INTO ad (title, price, description, locality_id, publisher_id, published_at, status, is_promoted)
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
        'SOLD',
        FALSE);