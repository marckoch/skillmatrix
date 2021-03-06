DROP TABLE developers IF EXISTS;
DROP TABLE skills IF EXISTS;
DROP TABLE experiences IF EXISTS;
DROP TABLE projects IF EXISTS;
DROP TABLE developers_to_projects IF EXISTS;

CREATE TABLE developers
(
    developer_id INTEGER IDENTITY PRIMARY KEY,
    first_name   VARCHAR(80) NOT NULL,
    last_name    VARCHAR_IGNORECASE(80) NOT NULL,
    title        VARCHAR(30)
);

CREATE TABLE skills
(
    skill_id INTEGER IDENTITY PRIMARY KEY,
    name     VARCHAR(80) NOT NULL,
    version  VARCHAR(30),
    alias    VARCHAR(30)
);

CREATE TABLE experiences
(
    experience_id INTEGER IDENTITY PRIMARY KEY,
    developer_id  INTEGER NOT NULL,
    skill_id      INTEGER NOT NULL,
    years         INTEGER NOT NULL,
    rating        INTEGER NOT NULL
);

CREATE TABLE projects
(
    project_id INTEGER IDENTITY PRIMARY KEY,
    name       VARCHAR(80) NOT NULL,
    since      VARCHAR(10) NOT NULL,
    until      VARCHAR(10) NOT NULL
);

CREATE TABLE developers_to_projects
(
    developer_id INTEGER,
    project_id   INTEGER
);