CREATE TABLE IF NOT EXISTS karma.tag
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    object_state character varying,
    create_date TIMESTAMP without time zone,
    update_date TIMESTAMP without time zone,
    delete_date TIMESTAMP without time zone,

    name character varying not null,
    description character varying,

    CONSTRAINT tag_pk PRIMARY KEY (id)
);
