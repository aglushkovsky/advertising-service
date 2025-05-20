--liquibase formatted sql

--changeset aglushkovsky:create_table_comment
CREATE TABLE IF NOT EXISTS comment
(
    id           bigserial PRIMARY KEY,
    author_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    ad_id        bigint                   NOT NULL REFERENCES ad ON DELETE CASCADE,
    created_at   timestamp with time zone NOT NULL,
    comment_text text                     NOT NULL
);