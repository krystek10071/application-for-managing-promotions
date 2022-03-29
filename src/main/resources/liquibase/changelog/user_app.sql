CREATE TABLE user_app
(
    id                          BIGINT                      NOT NULL,
    username                    VARCHAR(50)                 NOT NULL,
    password                    VARCHAR(50)                 NOT NULL,
    enabled                     BOOLEAN                     NOT NULL,
    role                        VARCHAR(10)                 NOT NULL,

    CONSTRAINT user_app_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE seq_user_app
    START WITH 1
    INCREMENT BY 1;