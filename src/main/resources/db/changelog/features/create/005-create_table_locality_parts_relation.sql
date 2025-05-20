--liquibase formatted sql

--changeset aglushkovsky:create_table_locality_parts_relation
CREATE TABLE IF NOT EXISTS locality_parts_relation
(
    id                     bigserial PRIMARY KEY,
    ancestor_locality_id   bigint  NOT NULL REFERENCES locality ON DELETE CASCADE,
    descendant_locality_id bigint  NOT NULL REFERENCES locality ON DELETE CASCADE,
    depth                  integer NOT NULL,
    UNIQUE (ancestor_locality_id, descendant_locality_id)
);