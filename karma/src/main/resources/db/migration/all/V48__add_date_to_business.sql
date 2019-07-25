ALTER TABLE karma.business
    ADD COLUMN create_date TIMESTAMP without time zone,
    ADD COLUMN update_date TIMESTAMP without time zone;

UPDATE karma.business
SET create_date = CURRENT_TIMESTAMP, update_date = CURRENT_TIMESTAMP;