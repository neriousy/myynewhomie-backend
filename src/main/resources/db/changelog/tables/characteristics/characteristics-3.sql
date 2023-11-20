--liquibase formatted sql
--changeset fhejmowski:3


INSERT INTO characteristics(id, charname)
VALUES (1, 'sleepTime'),
        (2, 'cooking'),
        (3, 'invitingFriends'),
        (4, 'timeSpentOutsideHome'),
        (5, 'characterType'),
        (6, 'talkativity'),
        (7, 'conciliatory'),
        (8, 'likesPets'),
        (9, 'hasPets'),
        (10, 'smokes'),
        (11, 'drinks'),
        (12, 'isStudent'),
        (13, 'works'),
        (14, 'acceptsPets'),
        (15, 'acceptsSmoking'),
        (16, 'preferedGender');
