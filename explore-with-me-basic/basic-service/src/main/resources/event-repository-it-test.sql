DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

INSERT INTO categories (id, name, name_lower) VALUES (1, 'cat1', 'cat1');

INSERT INTO users (id, name, email, email_lower, password) VALUES
(1, 'user1', 'owner@mail.ru', 'owner@mail.ru', 'kxfhvlxkjdfbdfbsb');

INSERT INTO events (id, annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid,
participant_limit, confirmed_requests, request_moderation, state, title)
VALUES
(1, 'anno1', 1, '2022-01-01T00:00:00', 'descr', '2100-01-01T00:00:00', 1, 1.0, 1.0, true, 1, 0, false, 'PENDING', 'title1'),
(2, 'anno2', 1, '2022-02-01T00:00:00', 'descr', '2100-02-01T00:00:00', 1, 1.0, 1.0, true, 1, 0, false, 'PUBLISHED', 'title2'),
(3, 'anno3', 1, '2022-03-01T00:00:00', 'descr', '2100-03-01T00:00:00', 1, 1.0, 1.0, true, 1, 0, false, 'PUBLISHED', 'title3'),
(4, 'anno4', 1, '2022-04-01T00:00:00', 'descr', '2100-04-01T00:00:00', 1, 1.0, 1.0, true, 1, 0, false, 'PUBLISHED', 'title4');
