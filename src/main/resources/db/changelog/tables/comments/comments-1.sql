--liquibase formatted sql
--changeset kacperfilipiuk:1


create table comments(
    id serial primary key,
    comment_author_id integer not null references _user(id),
    commented_user_id integer not null references _user(id),
    comment_text varchar(255) not null,
    date timestamp not null

);

CREATE SEQUENCE comments_seq
START 1
INCREMENT 50
MINVALUE 1
OWNED BY comments.id;