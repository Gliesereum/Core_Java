CREATE TABLE IF NOT EXISTS karma.filter_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying not null,
    title          character varying,
    CONSTRAINT filter_description_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.filter_attribute_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying not null,
    title          character varying,
    CONSTRAINT filter_attribute_description_pk PRIMARY KEY (id)
);
