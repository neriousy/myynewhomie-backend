--liquibase formatted sql
--changeset kacperfilipiuk:1


ALTER TABLE housing ADD COLUMN token varchar(255);