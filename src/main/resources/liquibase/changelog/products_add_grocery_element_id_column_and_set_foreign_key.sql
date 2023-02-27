
ALTER TABLE product ADD COLUMN grocery_element_id BIGINT NOT NULL;
ALTER TABLE product ADD CONSTRAINT grocery_element_id_fk FOREIGN KEY (grocery_element_id) REFERENCES grocery_element (id);