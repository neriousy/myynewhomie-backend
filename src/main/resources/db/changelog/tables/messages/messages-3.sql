--liquibase formatted sql
--changeset kacper.filipiuk:5

CREATE TABLE chat_message_and_users (
                          id SERIAL PRIMARY KEY,
                          sender_id INTEGER references _user(id),
                          receiver_id INTEGER references  _user(id),
                          message varchar(255),
                          date timestamp not null
);

CREATE SEQUENCE chat_message_and_users_seq
    START 1
    INCREMENT 50
    MINVALUE 1
    OWNED BY chat_message_and_users.id;