CREATE TABLE IF NOT EXISTS karma.working_space_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying NOT NULL,
    name          character varying,
    description   character varying,
    CONSTRAINT working_space_description_pk PRIMARY KEY (id)
);
