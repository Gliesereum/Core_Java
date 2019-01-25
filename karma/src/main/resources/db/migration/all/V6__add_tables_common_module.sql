CREATE TABLE IF NOT EXISTS karma.service (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   description character varying,
   service_type character varying,

   CONSTRAINT service_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_price (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   description character varying,
   price integer,
   duration integer,
   service_id uuid,
   business_id uuid,

   CONSTRAINT service_price_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_class (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    description character varying,
    order_index integer,
    service_type character varying,
    CONSTRAINT service_class_pk PRIMARY KEY (id)
);

CREATE TABLE karma.service_class_price (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  price_id uuid not null,
  service_class_id uuid not null,

  CONSTRAINT service_class_price_pk PRIMARY KEY (id),
  CONSTRAINT service_class_price_price_fk FOREIGN KEY (price_id) REFERENCES karma.service_price (id),
  CONSTRAINT service_class_price_service_class_fk FOREIGN KEY (service_class_id) REFERENCES karma.service_class (id)
);

CREATE TABLE IF NOT EXISTS karma.package (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   discount integer,
   duration integer,
   business_id uuid,

   CONSTRAINT package_pk PRIMARY KEY (id)
);

CREATE TABLE karma.package_service (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  package_id uuid not null,
  service_id uuid not null,

  CONSTRAINT package_service_pk PRIMARY KEY (id),
  CONSTRAINT package_service_package_fk FOREIGN KEY (package_id) REFERENCES karma.package (id),
  CONSTRAINT package_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service_price (id)
);

CREATE TABLE IF NOT EXISTS karma.work_time (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   day_of_week character varying,
   service_type character varying,
   business_id uuid,
   is_work boolean default false,
   from_time time without time zone,
   to_time time without time zone,

   CONSTRAINT work_time_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.work_space (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   status_space character varying,
   service_type character varying,
   index_number integer,
   worker_id uuid,
   business_id uuid,

   CONSTRAINT work_space_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.order (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   price integer,
   service_id uuid,
   record_id uuid,
   from_package boolean,

   CONSTRAINT order_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_price_interior (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   interior_type character varying,
   service_price_id uuid,

   CONSTRAINT service_price_interior_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.service_price_bodies (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   car_body character varying,
   service_price_id uuid,

   CONSTRAINT service_price_bodies_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.business (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   description character varying,
   logo_url character varying,
   address character varying,
   phone character varying,
   add_phone character varying,
   service_type character varying,
   latitude double precision,
   longitude double precision,
   corporation_id uuid,

   CONSTRAINT business_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.record (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   status_process character varying,
   status_pay character varying,
   status_record character varying,
   service_type character varying,
   price integer,
   description character varying,
   target_id uuid,
   package_id uuid,
   working_space_id uuid,
   business_id uuid,
   begin TIMESTAMP without time zone,
   finish TIMESTAMP without time zone,

   CONSTRAINT record_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.record_service (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  record_id uuid not null,
  service_id uuid not null,

  CONSTRAINT record_service_pk PRIMARY KEY (id),
  CONSTRAINT record_service_record_fk FOREIGN KEY (record_id) REFERENCES karma.record (id),
  CONSTRAINT record_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service_price (id)
);

CREATE TABLE karma.car_service_class (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  car_id uuid not null,
  service_class_id uuid not null,

  CONSTRAINT car_service_class_pk PRIMARY KEY (id),
  CONSTRAINT car_service_class_car_fk FOREIGN KEY (car_id) REFERENCES karma.car (id),
  CONSTRAINT car_service_class_service_class_fk FOREIGN KEY (service_class_id) REFERENCES karma.service_class (id)
);

