CREATE TABLE IF NOT EXISTS karma.record_message(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    message character varying NOT NULL,
    CONSTRAINT record_message_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.record_message_description(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    object_id uuid NOT NULL,
    language_code character varying NOT NULL,
    name character varying,
    description character varying,
    CONSTRAINT record_message_description_pk PRIMARY KEY (id)
);
