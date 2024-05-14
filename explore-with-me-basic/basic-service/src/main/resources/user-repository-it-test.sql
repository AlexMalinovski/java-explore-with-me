DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

INSERT INTO users (id, name, email, email_lower, password)
VALUES (1, 'user1', 'owner@mail.ru', 'owner@mail.ru', 'fhfjbfbfbdfgb'),
(2, 'user2', 'owner2@mail.ru', 'owner2@mail.ru', 'xdfhbsdjfb'),
(3, 'user3', 'owner3@mail.ru', 'owner3@mail.ru', 'dfjhbskdfjb'),
(4, 'user4', 'owner4@mail.ru', 'owner4@mail.ru', 'jshfvbkjskvbsjh');
