DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;

INSERT INTO categories (id, name, name_lower)
VALUES (1, 'cat1', 'cat1'), (2, 'cat2', 'cat2'), (3, 'cat3', 'cat3');