ALTER TABLE account.user
    ADD COLUMN update_date TIMESTAMP without time zone,
    ADD COLUMN delete_date TIMESTAMP without time zone,
    ADD COLUMN object_state character varying;

UPDATE account.user
SET update_date = create_date
WHERE update_date IS NULL;

UPDATE account.user
SET object_state = 'ACTIVE'
WHERE object_state IS NULL;