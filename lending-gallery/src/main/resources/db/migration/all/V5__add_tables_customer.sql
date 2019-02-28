CREATE TABLE IF NOT EXISTS lending_gallery.customer(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  user_id uuid,
  name character varying,
  place_birth character varying,
  date_birth TIMESTAMP without time zone,
  nationality character varying,
  passport character varying,
  position character varying,
  amount_investment integer,
  origin_funds character varying,
  customer_type character varying,

  CONSTRAINT customer_pk PRIMARY KEY (id)
);