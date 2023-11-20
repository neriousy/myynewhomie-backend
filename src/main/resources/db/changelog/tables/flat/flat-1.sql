--liquibase formatted sql
--changeset kacperfilipiuk:1


create table flat(
    id serial primary key,
    user_id integer not null references _user(id),
    description varchar(255) not null,
    latitude double precision not null,
    longitude double precision not null,
    people_count integer not null,
    room_count integer not null

);

CREATE SEQUENCE flat_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY flat.id;