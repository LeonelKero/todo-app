create database if not exists todo_db;

CREATE TABLE todos
(
    id          UUID NOT NULL,
    title       VARCHAR(255),
    description VARCHAR(255),
    is_done     BOOLEAN,
    CONSTRAINT pk_todos PRIMARY KEY (id)
);