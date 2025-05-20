--liquibase formatted sql

--changeset aglushkovsky:create_table_user_rate
CREATE TABLE IF NOT EXISTS user_rate
(
    id           bigserial PRIMARY KEY,
    author_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    value        double precision         NOT NULL,
    recipient_id bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    created_at   timestamp with time zone NOT NULL,
    UNIQUE (author_id, recipient_id)
);