CREATE TABLE grocery_list
(
    id              BIGINT              NOT NULL,
    create_date     DATE                NOT NULL,
    modify_date     DATE,
    user_app_id     BIGINT,

    CONSTRAINT grocery_list_pkey PRIMARY KEY (id),
    CONSTRAINT grocery_list_user_app_id_fk FOREIGN KEY (user_app_id) REFERENCES user_app (id)
);

CREATE SEQUENCE seq_grocery_list
    START WITH 1
    INCREMENT BY 1;