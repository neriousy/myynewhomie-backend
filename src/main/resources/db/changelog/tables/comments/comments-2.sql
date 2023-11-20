--liquibase formatted sql
--changeset kacperfilipiuk:1


alter table comments add column rating int not null default 1;