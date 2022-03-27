CREATE TABLE category_dictionary
(
    id              BIGINT              NOT NULL,
    name            VARCHAR(50),

    CONSTRAINT category_dictionary_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE seq_category_dictionary
    START WITH 1
    INCREMENT BY 1;