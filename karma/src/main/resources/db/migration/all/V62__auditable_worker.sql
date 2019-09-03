ALTER TABLE karma.worker
    ADD COLUMN create_date TIMESTAMP without time zone,
    ADD COLUMN update_date TIMESTAMP without time zone,
    ADD COLUMN delete_date TIMESTAMP without time zone,
    ADD COLUMN object_state character varying;

UPDATE karma.worker
SET create_date = CURRENT_TIMESTAMP
WHERE create_date IS NULL;

UPDATE karma.worker
SET update_date = CURRENT_TIMESTAMP
WHERE update_date IS NULL;

UPDATE karma.worker
SET object_state = 'ACTIVE'
WHERE object_state IS NULL;