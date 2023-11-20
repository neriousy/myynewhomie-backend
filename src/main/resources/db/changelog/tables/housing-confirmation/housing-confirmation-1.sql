--liquibase formatted sql
--changeset kacperfilipiuk:1

create table housing_confirmation (
    id serial primary key,
    user_to_confirm_id integer not null references _user(id),
    confirming_user_id integer not null references _user(id),
    token varchar(1000) not null,
    created_at timestamp not null,
    expires_at timestamp not null,
    confirmed_at timestamp
    );


CREATE SEQUENCE housing_confirmation_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY housing_confirmation.id;