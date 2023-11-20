--liquibase formatted sql
--changeset fhejmowski:4

INSERT INTO characteristics(id, charname)
VALUES (17, 'livesIn');
