CREATE TABLE IF NOT EXISTS karma.car (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   registration_number character varying,
   description character varying,
   interior character varying,
   car_body character varying,
   colour character varying,
   note character varying,
   brand_id uuid,
   model_id uuid,
   year_id uuid,
   user_id uuid,
   CONSTRAINT car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.year_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name integer,
    CONSTRAINT year_car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.brand_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    CONSTRAINT brand_car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.model_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    brand_id uuid,
    name character varying,
    CONSTRAINT model_car_pk PRIMARY KEY (id)
);