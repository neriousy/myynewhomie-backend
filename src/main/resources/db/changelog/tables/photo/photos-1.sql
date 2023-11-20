--liquibase formatted sql
--changeset kacper.filipiuk:1

CREATE TABLE photos (
                        id SERIAL PRIMARY KEY,
                        user_id  integer references _user(id),
                        name TEXT,
                        data BYTEA
);


CREATE SEQUENCE photos_seq
    START 1
    INCREMENT 50
    MINVALUE 1
    OWNED BY photos.id;