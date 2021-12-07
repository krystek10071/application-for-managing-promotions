CREATE TABLE users
(
    id                          BIGINT                      NOT NULL,
    username                    VARCHAR(50)                 NOT NULL,
    password                    VARCHAR(50)                 NOT NULL,
    enabled                     BOOLEAN                     NOT NULL,

    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE seq_user
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 923333333333333
    CACHE 1;