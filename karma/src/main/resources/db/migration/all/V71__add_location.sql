CREATE TABLE IF NOT EXISTS karma.country
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    centre_latitude double precision,
    centre_longitude double precision,
    CONSTRAINT country_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS karma.city
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    country_id uuid,
    name character varying,
    centre_latitude double precision,
    centre_longitude double precision,
    CONSTRAINT city_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS karma.district
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    city_id uuid,
    name character varying,
    CONSTRAINT district_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS karma.geo_position
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    object_id uuid,
    latitude double precision,
    longitude double precision,
    CONSTRAINT geo_position_pk PRIMARY KEY (id)
);
