CREATE TABLE favourite_product
(
    id                          BIGINT                      NOT NULL,
    is_observe                  BOOLEAN                     NOT NULL,
    start_date                  DATE                        NOT NULL,
    end_date                    DATE,
    product_id                  BIGINT                      NOT NULL,
    user_app_id                 BIGINT                      NOT NULL,

    CONSTRAINT favourite_product_pkey PRIMARY KEY (id),
    CONSTRAINT favourite_product_user_app_id_fk FOREIGN KEY (user_app_id) REFERENCES user_app (id)
);

CREATE SEQUENCE seq_favourite_product
    START WITH 1
    INCREMENT BY 1;