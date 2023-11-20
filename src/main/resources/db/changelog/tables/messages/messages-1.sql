--liquibase formatted sql
--changeset kacper.filipiuk:3

CREATE TABLE chat_channel (
                          id SERIAL PRIMARY KEY,
                          sender_id INTEGER NOT NULL references _user(id),
                          receiver_id INTEGER NOT NULL references _user(id)
);



CREATE SEQUENCE chat_channel_seq
    START 1
    INCREMENT 50
    MINVALUE 1
    OWNED BY chat_channel.id;