--liquibase formatted sql
--changeset fhejmowski:1

create table confirmationtoken (
    id serial primary key,
    token varchar(1000) not null,
    created_at timestamp not null,
    expires_at timestamp not null,
    confirmed_at timestamp,
    user_id integer REFERENCES _user(id)
    );


CREATE SEQUENCE confirmationtoken_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY confirmationtoken.id;