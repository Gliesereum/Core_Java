CREATE TABLE IF NOT EXISTS lending_gallery.investor_offer(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),

  customer_id uuid,
  art_bond_id uuid,
  sum_investment integer,
  state_type character varying,
  create_date TIMESTAMP without time zone DEFAULT NOW(),

  CONSTRAINT investor_offer_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lending_gallery.borrower_offer(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),

  customer_id uuid,
  create_date TIMESTAMP without time zone DEFAULT NOW(),
  description character varying,
  state_type character varying,

  CONSTRAINT borrower_offer_pk PRIMARY KEY (id)
);