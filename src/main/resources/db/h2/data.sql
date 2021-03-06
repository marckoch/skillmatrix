INSERT INTO developers VALUES (1, 'Peter', 'Parker', 'Senior Technical Consultant');
INSERT INTO developers VALUES (2, 'Tony', 'Stark', 'Dr.');
INSERT INTO developers VALUES (3, 'Kann', 'Nix', 'Junior Consultant');
INSERT INTO developers VALUES (4, 'Thorsten', 'Hansen', 'Expert Technical Consultant');
INSERT INTO developers VALUES (5, 'Mirco', 'Pehler', null);
INSERT INTO developers VALUES (6, 'Frontend', 'Hero', null);
INSERT INTO developers VALUES (7, 'Cloud', 'Hero', null);
INSERT INTO developers VALUES (8, 'Backend', 'Hero', null);
INSERT INTO developers VALUES (9, 'Hubert Blaine', 'Wolfeschlegelsteinhausenbergerdorff', 'Test für lange Namen');
INSERT INTO developers VALUES (10, 'Robert', 'Martin', 'Uncle Bob');
INSERT INTO developers VALUES (11, 'Vlad', 'Mihalcea', 'Java Champion');
INSERT INTO developers VALUES (12, 'Johnny', 'Docker', null);

INSERT INTO skills VALUES (1, 'Java', '11', 'Java SE');
INSERT INTO skills VALUES (2, 'Spring', '5', null);
INSERT INTO skills VALUES (3, 'Spring Boot', '2.5', null);
INSERT INTO skills VALUES (4, 'Spring Boot', '2.0', null);
INSERT INTO skills VALUES (5, 'Java', '17', null);
INSERT INTO skills VALUES (6, 'Java', '8', 'Java SE');
INSERT INTO skills VALUES (7, 'Java', '13', null);
INSERT INTO skills VALUES (8, 'Docker', null, null);
INSERT INTO skills VALUES (9, 'Kubernetes', null, 'K8s');
INSERT INTO skills VALUES (10, 'Openshift', '3', null);
INSERT INTO skills VALUES (11, 'Openshift', '4', null);
INSERT INTO skills VALUES (12, 'React', null, null);
INSERT INTO skills VALUES (13, 'Angular', null, null);
INSERT INTO skills VALUES (14, 'PHP', null, null);
INSERT INTO skills VALUES (15, 'Glassfish', null, null);
INSERT INTO skills VALUES (16, 'Payara', null, null);
INSERT INTO skills VALUES (17, 'HTML', '5', 'HTML5');
INSERT INTO skills VALUES (18, 'CSS', null, null);
INSERT INTO skills VALUES (19, 'SQL', null, null);
INSERT INTO skills VALUES (20, 'JPA', null, null);
INSERT INTO skills VALUES (21, 'Hibernate', null, null);
INSERT INTO skills VALUES (22, 'Amazon Webservices', null, 'AWS');
INSERT INTO skills VALUES (23, 'Bootstrap', null, null);
INSERT INTO skills VALUES (24, 'Javascript', null, 'ECMA Script');
INSERT INTO skills VALUES (25, 'Google Cloud', null, null);
INSERT INTO skills VALUES (26, 'Azure', null, null);

INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (1, 1, 1, 5, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (2, 1, 2, 3, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (3, 1, 3, 2, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (4, 2, 2, 12, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (5, 2, 3, 2, 2);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (6, 1, 5, 1, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (7, 1, 6, 4, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (8, 1, 13, 2, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (9, 4, 20, 8, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (10, 4, 21, 9, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (11, 4, 19, 19, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (12, 5, 2, 9, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (13, 5, 3, 8, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (14, 1, 4, 2, 3);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (15, 6, 17, 15, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (16, 6, 18, 15, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (17, 6, 23, 7, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (18, 6, 24, 13, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (19, 7, 22, 12, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (20, 7, 25, 10, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (21, 7, 26, 5, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (22, 8, 6, 6, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (23, 7, 8, 10, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (24, 7, 9, 10, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (25, 8, 14, 19, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (26, 7, 10, 3, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (27, 7, 11, 1, 4);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (28, 10, 1, 20, 5);
INSERT INTO experiences (experience_id, developer_id, skill_id, years, rating) VALUES (29, 11, 20, 15, 5);

INSERT INTO projects VALUES (1, 'VKB', '2019-03', '2025-12');

INSERT INTO developers_to_projects VALUES (1, 1);