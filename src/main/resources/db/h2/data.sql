INSERT INTO developers VALUES (1, 'Peter', 'Parker', null);

INSERT INTO skills VALUES (1, 'Java', '11', null);
INSERT INTO skills VALUES (2, 'Spring', '5', null);
INSERT INTO skills VALUES (3, 'Spring Boot', '2.5', null);

INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (1, 1, 1, 5, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (2, 1, 2, 3, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (3, 1, 3, 2, 3);
