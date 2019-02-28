CREATE TABLE IF NOT EXISTS lending_gallery.content(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),

  title character varying,
  description character varying,
  content_type character varying,
  create_date TIMESTAMP without time zone DEFAULT NOW(),

  CONSTRAINT content_pk PRIMARY KEY (id)
);
