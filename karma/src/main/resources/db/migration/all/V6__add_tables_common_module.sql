CREATE TABLE IF NOT EXISTS karma.service (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   description character varying,
   type character varying,

   CONSTRAINT service_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_price (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   car_body character varying,
   name character varying,
   interior_type character varying,
   price integer,
   duration integer,
   service_id uuid,
   business_service_id uuid,

   CONSTRAINT service_price_pk PRIMARY KEY (id)
);

CREATE TABLE karma.service_class_price (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  price_id uuid not null,
  service_class_car_id uuid not null,

  CONSTRAINT service_class_price_pk PRIMARY KEY (id),
  CONSTRAINT service_class_price_price_fk FOREIGN KEY (price_id) REFERENCES karma.service_price (id),
  CONSTRAINT service_class_price_service_class_fk FOREIGN KEY (service_class_car_id) REFERENCES karma.service_class_car (id)
);

CREATE TABLE IF NOT EXISTS karma.package (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   discount integer,
   duration integer,

   CONSTRAINT package_pk PRIMARY KEY (id)
);

CREATE TABLE karma.package_service (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  package_id uuid not null,
  service_id uuid not null,

  CONSTRAINT package_service_pk PRIMARY KEY (id),
  CONSTRAINT package_service_package_fk FOREIGN KEY (package_id) REFERENCES karma.package (id),
  CONSTRAINT package_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service (id)
);

CREATE TABLE IF NOT EXISTS karma.work_time (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   day_of_week character varying,
   car_service_type character varying,
   business_service_id uuid,
   is_work boolean default false,
   from_time time without time zone,
   to_time time without time zone,

   CONSTRAINT work_time_pk PRIMARY KEY (id)
);