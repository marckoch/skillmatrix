INSERT INTO developers VALUES (1, 'Peter', 'Parker', null);
INSERT INTO developers VALUES (2, 'Tony', 'Stark', 'Dr.');
INSERT INTO developers VALUES (3, 'Kann', 'Nix', null);

INSERT INTO skills VALUES (1, 'Java', '11', 'Java SE');
INSERT INTO skills VALUES (2, 'Spring', '5', null);
INSERT INTO skills VALUES (3, 'Spring Boot', '2.5', null);
INSERT INTO skills VALUES (4, 'Spring Boot', '2.0', null);
INSERT INTO skills VALUES (5, 'Java', '17', null);
INSERT INTO skills VALUES (6, 'Java', '8', 'Java SE');

INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (1, 1, 1, 5, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (2, 1, 2, 3, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (3, 1, 3, 2, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (4, 2, 2, 12, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (5, 2, 3, 2, 2);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (6, 1, 5, 1, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (7, 1, 6, 4, 5);
