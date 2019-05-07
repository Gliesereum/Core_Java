ALTER TABLE karma.business_category
    ADD COLUMN active boolean default true;

ALTER TABLE karma.business_category
    ADD COLUMN order_index integer;

UPDATE karma.business_category
SET active = true;
