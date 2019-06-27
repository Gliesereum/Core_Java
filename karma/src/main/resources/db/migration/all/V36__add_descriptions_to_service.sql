CREATE TABLE IF NOT EXISTS karma.package_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying NOT NULL,
    name          character varying,
    CONSTRAINT package_description_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_class_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying NOT NULL,
    name          character varying,
    description   character varying,
    CONSTRAINT service_class_description_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying NOT NULL,
    name          character varying,
    description   character varying,
    CONSTRAINT service_description_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_price_description
(
    id            uuid              NOT NULL DEFAULT uuid_generate_v4(),
    object_id     uuid              NOT NULL,
    language_code character varying NOT NULL,
    name          character varying,
    description   character varying,
    CONSTRAINT service_price_description_pk PRIMARY KEY (id)
);
