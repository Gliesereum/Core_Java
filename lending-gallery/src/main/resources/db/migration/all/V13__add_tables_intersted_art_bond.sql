CREATE TABLE IF NOT EXISTS lending_gallery.interested_art_bond(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),

  customer_id uuid,
  art_bond_id uuid,

  CONSTRAINT interested_art_bond_pk PRIMARY KEY (id)
);

