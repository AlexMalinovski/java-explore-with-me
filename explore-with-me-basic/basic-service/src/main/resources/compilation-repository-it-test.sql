DELETE FROM events_compilations;
DELETE FROM compilations;
DELETE FROM participation;
DELETE FROM events;
DELETE FROM users;
DELETE FROM categories;


INSERT INTO compilations (id, pinned, title, title_lower)
VALUES (1, true, 'TitLe1', 'title1'),
(2, false, 'TitLe2', 'title2'),
(3, true, 'TitLe3', 'title3'),
(4, false, 'TitLe4', 'title4'),
(5, false, 'TitLe5', 'title5');