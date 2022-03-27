CREATE TABLE product
(
    id                      BIGINT                  NOT NULL,
    product_name            VARCHAR(50)             NOT NULL,
    description             VARCHAR(50),
    price                   DECIMAL(19,2)           NOT NULL,
    category_id             BIGINT,
    shop_id                 BIGINT,

    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT product_category_id_fk FOREIGN KEY (category_id) REFERENCES category_dictionary (id),
    CONSTRAINT product_shop_id_fk FOREIGN KEY (shop_id) REFERENCES shop (id)
);

CREATE SEQUENCE seq_product
    START WITH 1
    INCREMENT BY 1;