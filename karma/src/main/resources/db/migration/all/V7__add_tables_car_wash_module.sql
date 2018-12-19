CREATE TABLE IF NOT EXISTS karma.car_wash (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   name character varying,
   description character varying,
   logo_url character varying,
   address character varying,
   phone character varying,
   add_phone character varying,
   latitude double precision,
   longitude double precision,
   user_business_id uuid,
   work_time_id uuid,

   CONSTRAINT car_wash_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.car_wash_record (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   status_washing character varying,
   status_pay character varying,
   status_record character varying,
   price integer,
   description character varying,
   car_id uuid,
   package_id uuid,
   working_space_id uuid,
   car_wash_id uuid,
   begin_time time without time zone,
   finish_time time without time zone,
   date date,

   CONSTRAINT car_wash_record_pk PRIMARY KEY (id)
);

CREATE TABLE karma.car_wash_record_service (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  car_wash_record_id uuid not null,
  service_id uuid not null,

  CONSTRAINT car_wash_record_service_pk PRIMARY KEY (id),
  CONSTRAINT car_wash_record_service_car_wash_record_fk FOREIGN KEY (car_wash_record_id) REFERENCES karma.car_wash_record (id),
  CONSTRAINT car_wash_record_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service (id)
);

