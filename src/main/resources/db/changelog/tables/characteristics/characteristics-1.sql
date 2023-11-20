--liquibase formatted sql
--changeset fhejmowski:1


create table characteristics(
    id serial primary key,
    charname varchar(200) not null
);

CREATE SEQUENCE characteristics_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY characteristics.id;