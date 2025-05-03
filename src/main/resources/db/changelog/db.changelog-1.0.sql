--liquibase formatted sql

--changeset aglushkovsky:1
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

--changeset aglushkovsky:2
CREATE TABLE IF NOT EXISTS user_rate
(
    id           bigserial PRIMARY KEY,
    author_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    value        double precision         NOT NULL,
    recipient_id bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    created_at   timestamp with time zone NOT NULL,
    UNIQUE (author_id, recipient_id)
);

--changeset aglushkovsky:3
CREATE TABLE IF NOT EXISTS message
(
    id           bigserial PRIMARY KEY,
    sender_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    receiver_id  bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    sent_at      timestamp with time zone NOT NULL,
    message_text text                     NOT NULL
);

--changeset aglushkovsky:4
CREATE TABLE IF NOT EXISTS locality
(
    id                 bigserial PRIMARY KEY,
    name               varchar(255) NOT NULL,
    parent_locality_id bigint REFERENCES locality ON DELETE CASCADE,
    type               varchar(20)  NOT NULL,
    UNIQUE (name, parent_locality_id, type)
);

--changeset aglushkovsky:5
CREATE TABLE IF NOT EXISTS locality_parts_relation
(
    id                     bigserial PRIMARY KEY,
    ancestor_locality_id   bigint  NOT NULL REFERENCES locality ON DELETE CASCADE,
    descendant_locality_id bigint  NOT NULL REFERENCES locality ON DELETE CASCADE,
    depth                  integer NOT NULL,
    UNIQUE (ancestor_locality_id, descendant_locality_id)
);

--changeset aglushkovsky:6
CREATE TABLE IF NOT EXISTS ad
(
    id           bigserial PRIMARY KEY,
    title        varchar(128) NOT NULL,
    price        bigint       NOT NULL,
    description  text,
    locality_id  bigint       NOT NULL REFERENCES locality,
    publisher_id bigint       NOT NULL REFERENCES "user" ON DELETE CASCADE,
    published_at timestamp with time zone,
    is_promoted  boolean      NOT NULL
);

--changeset aglushkovsky:7
CREATE TABLE IF NOT EXISTS comment
(
    id           bigserial PRIMARY KEY,
    author_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    ad_id        bigint                   NOT NULL REFERENCES ad ON DELETE CASCADE,
    created_at   timestamp with time zone NOT NULL,
    comment_text text                     NOT NULL
);
