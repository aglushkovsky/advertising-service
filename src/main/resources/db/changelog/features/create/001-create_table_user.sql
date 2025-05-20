--liquibase formatted sql

--changeset aglushkovsky:create_table_user
CREATE TABLE IF NOT EXISTS "user"
(
    id            bigserial PRIMARY KEY,
    login         varchar(50)      NOT NULL UNIQUE,
    password_hash varchar(70)      NOT NULL,
    email         varchar(50) UNIQUE,
    phone_number  varchar(15) UNIQUE,
    role          varchar(30)      NOT NULL,
    total_rating  double precision NOT NULL
);