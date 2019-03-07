CREATE TABLE IF NOT EXISTS lending_gallery.account(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  CONSTRAINT account_pk PRIMARY KEY (id)
);