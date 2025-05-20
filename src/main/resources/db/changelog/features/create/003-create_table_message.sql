--liquibase formatted sql

--changeset aglushkovsky:create_table_message
CREATE TABLE IF NOT EXISTS message
(
    id           bigserial PRIMARY KEY,
    sender_id    bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    receiver_id  bigint                   NOT NULL REFERENCES "user" ON DELETE CASCADE,
    sent_at      timestamp with time zone NOT NULL,
    message_text text                     NOT NULL
);