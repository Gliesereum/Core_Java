CREATE TABLE IF NOT EXISTS lending_gallery.art_bond (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   price integer,
   dividend_percent integer,
   reward_percent integer,
   name character varying,
   description character varying,
   author character varying,
   execution character varying,
   size character varying,
   dated character varying,
   location character varying,
   state character varying,
   origin character varying,
   exhibitions character varying,
   literature character varying,
   status_type character varying,
   special_service_type character varying,
   CONSTRAINT art_bond_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lending_gallery.art_bond_tag(
    art_bond_id uuid not null,
    tag character varying not null,
    CONSTRAINT art_bond_tag_fk FOREIGN KEY (art_bond_id) REFERENCES lending_gallery.art_bond (id)
);

CREATE TABLE IF NOT EXISTS lending_gallery.media(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  object_id uuid NOT NULL,
  file_id uuid NOT NULL,
  title character varying,
  description character varying,
  block_media_type character varying,
  CONSTRAINT media_pk PRIMARY KEY (id)
);