ALTER TABLE karma.record
    ADD COLUMN specified_working_space boolean not null default false;

CREATE TABLE IF NOT EXISTS karma.user_pin_code
(
    id                 uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id uuid,
    pin_code integer,
    CONSTRAINT user_pin_code_pk PRIMARY KEY (id)
);