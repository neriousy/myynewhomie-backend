--liquibase formatted sql
--changeset kacperfilipiuk:1

ALTER TABLE _user ADD COLUMN still_looking boolean DEFAULT true;