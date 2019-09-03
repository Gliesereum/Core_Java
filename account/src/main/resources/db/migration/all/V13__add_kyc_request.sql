CREATE TABLE account.kyc_field
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  name character varying,
  title character varying,
  description character varying,
  placeholder character varying,
  field_type character varying,
  kyc_request_type character varying,

  CONSTRAINT kyc_field_pk PRIMARY KEY (id)
);

CREATE TABLE account.kyc_request
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  object_id uuid,
  kyc_request_type character varying,
  kyc_status character varying,
  comment character varying,

  CONSTRAINT kyc_request_pk PRIMARY KEY (id)
);

CREATE TABLE account.kyc_request_field
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  kyc_field_id uuid,
  kyc_request_id uuid,
  value character varying,

  CONSTRAINT kyc_request_field_pk PRIMARY KEY (id)
)