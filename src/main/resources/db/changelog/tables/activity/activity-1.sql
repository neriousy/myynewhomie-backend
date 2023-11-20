--liquibase formatted sql
--changeset fhejmowski:1

create table activity (
    email varchar(255) unique REFERENCES _user(email),
    last_active timestamp
);