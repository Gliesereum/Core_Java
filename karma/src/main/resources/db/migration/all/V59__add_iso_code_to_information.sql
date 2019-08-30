ALTER TABLE karma.information
    ADD COLUMN iso_code character varying;

UPDATE karma.information
SET iso_code = 'ru'
WHERE information.iso_code IS NULL;