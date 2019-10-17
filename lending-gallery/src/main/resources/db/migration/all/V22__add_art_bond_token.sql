CREATE TABLE IF NOT EXISTS lending_gallery.art_bond_token
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    art_bond_id  uuid NOT NULL,
    stock_count  integer,
    blockchain   character varying,

    CONSTRAINT art_bond_token_pk PRIMARY KEY (id)
);
