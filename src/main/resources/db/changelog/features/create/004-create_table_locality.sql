--liquibase formatted sql

--changeset aglushkovsky:create_table_locality
CREATE TABLE IF NOT EXISTS locality
(
    id   bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    type varchar(20)  NOT NULL
);