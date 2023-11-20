--liquibase formatted sql
--changeset kacperfilipiuk:1


create table flat_photos(
    id serial primary key,
    flat_id integer references flat(id),
    file_name varchar(255),
    data BYTEA
);

CREATE SEQUENCE flat_photos_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY flat_photos.id;