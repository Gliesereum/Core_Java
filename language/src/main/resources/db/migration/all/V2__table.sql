CREATE TABLE IF NOT EXISTS language.package
(
    id        uuid NOT NULL DEFAULT uuid_generate_v4(),
    module    character varying,
    label     character varying,
    iso_key   character varying,
    icon      character varying,
    direction character varying,
    CONSTRAINT package_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS language.phrase
(
    id        uuid NOT NULL DEFAULT uuid_generate_v4(),
    package_id uuid,
    label     character varying,
    key       character varying,
    CONSTRAINT phrase_pk PRIMARY KEY (id)
);