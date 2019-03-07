CREATE TABLE IF NOT EXISTS lending_gallery.dividend(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  CONSTRAINT dividend_pk PRIMARY KEY (id)
);