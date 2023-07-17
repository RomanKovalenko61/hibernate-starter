CREATE TABLE users
(
    firstname  VARCHAR(128),
    lastname   VARCHAR(128),
    birth_date DATE,
    username   VARCHAR(128) UNIQUE,
    role       VARCHAR(32),
    info       JSONB,
    PRIMARY KEY (firstname, lastname, birth_date)
);

-- CREATE SEQUENCE users_id_seq OWNED BY public.users.id;

-- DROP SEQUENCE users_id_seq;

DROP TABLE users;

-- for strategy = GenerationType.TABLE
-- В случае отсутствия в БД сиквенсов и автогенерируемых ключей
CREATE TABLE all_sequence
(
    table_name VARCHAR(32) PRIMARY KEY,
    pk_value   BIGINT NOT NULL
);