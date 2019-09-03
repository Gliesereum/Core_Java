ALTER TABLE account.corporation
    ADD COLUMN create_date TIMESTAMP without time zone,
    ADD COLUMN update_date TIMESTAMP without time zone;

UPDATE account.corporation
SET create_date = CURRENT_TIMESTAMP, update_date = CURRENT_TIMESTAMP;