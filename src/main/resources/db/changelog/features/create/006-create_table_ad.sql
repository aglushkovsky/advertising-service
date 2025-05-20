--liquibase formatted sql

--changeset aglushkovsky:create_table_ad
CREATE TABLE IF NOT EXISTS ad
(
    id           bigserial PRIMARY KEY,
    title        varchar(128) NOT NULL,
    price        bigint       NOT NULL,
    description  text,
    locality_id  bigint       NOT NULL REFERENCES locality,
    publisher_id bigint       NOT NULL REFERENCES "user" ON DELETE CASCADE,
    published_at timestamp with time zone,
    status       varchar(30)  NOT NULL,
    is_promoted  boolean      NOT NULL
);