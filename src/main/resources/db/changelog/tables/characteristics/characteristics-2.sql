--liquibase formatted sql
--changeset fhejmowski:2


create table user_characteristics(
    user_id int not null REFERENCES _user(id),
    char_id int not null REFERENCES characteristics(id),
    val varchar(200) not null,
    PRIMARY KEY (user_id, char_id)
);