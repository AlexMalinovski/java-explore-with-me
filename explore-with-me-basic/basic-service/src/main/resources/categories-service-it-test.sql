DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

INSERT INTO categories (id, name, name_lower) VALUES (1, 'cat1', 'cat1'), (2, 'cat2', 'cat2');

INSERT INTO users (id, name, email, email_lower, password) VALUES (1, 'user1', 'owner@mail.ru', 'owner@mail.ru', 'dblksjdnbls');

INSERT INTO events (id, annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid,
participant_limit, confirmed_requests, request_moderation, state, title)
VALUES (1, 'anno', 1, '2022-01-01T00:00:00', 'descr', '2122-01-01T00:00:00', 1, 1.0, 1.0, true, 0, 0, true, 'PENDING', 'title');