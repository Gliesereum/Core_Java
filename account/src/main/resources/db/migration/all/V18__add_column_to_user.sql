ALTER TABLE account.user
    ADD COLUMN last_sign_in TIMESTAMP without time zone,
    ADD COLUMN last_activity TIMESTAMP without time zone,
    ADD COLUMN create_date TIMESTAMP without time zone;