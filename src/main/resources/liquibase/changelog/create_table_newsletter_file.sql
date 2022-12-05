CREATE TABLE newsletter_file
(
    id              BIGINT          NOT NULL,
    file_name       VARCHAR(50)     NOT NULL,
    path            VARCHAR(50)     NOT NULL,
    extension       VARCHAR(10)     NOT NULL,
    created_date    DATE,
    user_id         BIGINT,

    CONSTRAINT newsletter_file_pk PRIMARY KEY (id)
);

CREATE SEQUENCE seq_newsletter_file
    START WITH 1
    INCREMENT BY 1;