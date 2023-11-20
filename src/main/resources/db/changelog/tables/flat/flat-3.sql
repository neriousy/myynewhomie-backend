--liquibase formatted sql
--changeset kacperfilipiuk:1

ALTER TABLE flat ADD COLUMN search_option integer NOT NULL DEFAULT -1;