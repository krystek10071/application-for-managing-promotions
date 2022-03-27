CREATE TABLE grocery_element
(
    id                   BIGINT              NOT NULL,
    name                 VARCHAR(50),
    unit                 VARCHAR(10),
    amount               INTEGER,
    grocery_list_id      BIGINT,

    CONSTRAINT grocery_element_pkey PRIMARY KEY (id),
    CONSTRAINT grocery_element_grocery_list_id_fk FOREIGN KEY (grocery_list_id) REFERENCES grocery_list (id)
);

CREATE SEQUENCE seq_grocery_element
    START WITH 1
    INCREMENT BY 1;