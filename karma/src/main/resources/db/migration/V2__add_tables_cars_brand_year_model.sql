CREATE TABLE IF NOT EXISTS karma.car (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   registration_number character varying,
   description character varying,
   interior character varying,
   car_body character varying,
   colour character varying,
   brand_id uuid,
   model_id uuid,
   year_id uuid,
   user_id uuid,
   CONSTRAINT car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.year_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    year_value integer,
    CONSTRAINT year_car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.brand_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    id_car integer,
    name character varying,
    CONSTRAINT brand_car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.model_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    id_model integer,
    brand_id_int integer,
    brand_id uuid,
    name character varying,
    CONSTRAINT model_car_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_class_car (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    description character varying,
    CONSTRAINT service_class_car_pk PRIMARY KEY (id)
);

CREATE TABLE karma.car_service_class_car (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  car_id uuid not null,
  service_class_car_id uuid not null,

  CONSTRAINT car_service_class_car_pk PRIMARY KEY (id),
  CONSTRAINT car_service_class_car_car_fk FOREIGN KEY (car_id) REFERENCES karma.car (id),
  CONSTRAINT car_service_class_car_service_class_car_fk FOREIGN KEY (service_class_car_id) REFERENCES karma.service_class_car (id)
);
