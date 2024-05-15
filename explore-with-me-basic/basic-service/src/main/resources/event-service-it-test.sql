DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

INSERT INTO categories (id, name, name_lower) VALUES (1, 'cat1', 'cat1');

INSERT INTO users (id, name, email, email_lower, password) VALUES
(1, 'user1', 'owner@mail.ru', 'owner@mail.ru', 'xfkxfkjnkjxflj'),
(2, 'user2', 'requester2@mail.ru', 'requester2@mail.ru', 'jdhbvkjhfvbhjdf'),
(3, 'user3', 'requester3@mail.ru', 'requester3@mail.ru', 'jkfhbgkjdfbk');

INSERT INTO events (id, annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid,
participant_limit, confirmed_requests, request_moderation, state, title)
VALUES (1, 'anno', 1, '2022-01-01T00:00:00', 'descr', '2100-01-01T00:00:00', 1, 1.0, 1.0, true, 1, 0, false, 'PUBLISHED', 'title');

INSERT INTO participation (id, created_on, requester_id, event_id, status)
VALUES (1, '2022-01-01T00:00:00', 2, 1, 'PENDING'),
(2, '2022-01-01T00:00:01', 3, 1, 'PENDING');