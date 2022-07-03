CREATE TABLE newsletter_file
(
    id              BIGINT          NOT NULL,
    file_name       VARCHAR(50)     NOT NULL,
    path            VARCHAR(50)     NOT NULL,
    extension       DATE            NOT NULL,
    date_from       DATE,
    date_to         DATE,
    user_id         BIGINT,

    CONSTRAINT newsletter_file_pk PRIMARY KEY (id),
    CONSTRAINT newsletter_file_user_id_fk FOREIGN KEY (user_id) REFERENCES user_app (id)
);

CREATE SEQUENCE seq_newsletter_file
    START WITH 1
    INCREMENT BY 1;