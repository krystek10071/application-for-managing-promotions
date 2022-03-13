CREATE TABLE users
(
    id                          BIGINT                      NOT NULL,
    username                    VARCHAR(50)                 NOT NULL,
    password                    VARCHAR(50)                 NOT NULL,
    enabled                     BOOLEAN                     NOT NULL,

    CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1;