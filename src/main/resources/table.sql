CREATE TABLE users
(
    username VARCHAR(128) PRIMARY KEY ,
    firstname VARCHAR(128),
    lastname VARCHAR(128),
    birth_date DATE,
    role VARCHAR(32),
    info JSONB
);

DROP TABLE users;