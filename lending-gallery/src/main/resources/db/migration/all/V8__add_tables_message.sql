CREATE TABLE IF NOT EXISTS lending_gallery.message(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),

  customer_id uuid,
  theme character varying,
  message character varying,
  section_type character varying,
  create_date TIMESTAMP without time zone DEFAULT NOW(),

  CONSTRAINT message_pk PRIMARY KEY (id)
);
