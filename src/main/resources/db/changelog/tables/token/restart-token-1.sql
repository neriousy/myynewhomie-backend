--liquibase formatted sql
--changeset kacper.filipiuk:2

create table restart_token (
                                   id serial primary key,
                                   token varchar(1000) not null,
                                   expiry_date timestamp not null,
                                   user_id integer REFERENCES _user(id)
);


CREATE SEQUENCE restart_token_seq
    START 1
    INCREMENT 50
    MINVALUE 1
    OWNED BY restart_token.id;