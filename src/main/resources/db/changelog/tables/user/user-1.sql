--liquibase formatted sql
--changeset kacper.filipiuk:2

create table _user (
    id serial primary key,
    firstname varchar(255)not null,
    lastname varchar(255) not null,
    email varchar(255) unique,
    password varchar(255) not null,
    age int not null,
    gender varchar(255) not null,
    phonenumber varchar(20) not null,
    enabled boolean not null,
    role varchar(30) not null,
    status varchar(30) not null,
    description varchar(255)
);

CREATE SEQUENCE _user_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY _user.id;