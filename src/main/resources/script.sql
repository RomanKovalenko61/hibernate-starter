CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    firstname  VARCHAR(128),
    lastname   VARCHAR(128),
    birth_date DATE,
    username   VARCHAR(128) UNIQUE,
    role       VARCHAR(32),
    info       JSONB,
    company_id INT REFERENCES company (id) ON DELETE CASCADE
);

CREATE TABLE company
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE profile
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT NOT NULL UNIQUE REFERENCES users (id),
    street   VARCHAR(128),
    language CHAR(2)
);

DROP TABLE profile;

-- CREATE SEQUENCE users_id_seq OWNED BY public.users.id;

-- DROP SEQUENCE users_id_seq;

DROP TABLE users;
DROP TABLE company;

-- for strategy = GenerationType.TABLE
-- В случае отсутствия в БД сиквенсов и автогенерируемых ключей
CREATE TABLE all_sequence
(
    table_name VARCHAR(32) PRIMARY KEY,
    pk_value   BIGINT NOT NULL
);