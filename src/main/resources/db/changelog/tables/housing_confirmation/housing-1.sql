--liquibase formatted sql
--changeset kacperfilipiuk:1


create table housing (
                     id serial primary key,
                     confirming_users numeric not null,
                     confirmed_by_users numeric not null,
                     housing_status integer not null,
                     status_date timestamp not null

);

CREATE SEQUENCE housing_seq
    START 1
    INCREMENT 50
    MINVALUE 1
    OWNED BY housing.id;