CREATE TABLE shop
(
    id              BIGINT              NOT NULL,
    shop_name       VARCHAR(50),
    postal_code     VARCHAR(10),
    open_hour       VARCHAR(10),
    close_hour      VARCHAR(10),

    CONSTRAINT shop_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE seq_shops
    START WITH 1
    INCREMENT BY 1;