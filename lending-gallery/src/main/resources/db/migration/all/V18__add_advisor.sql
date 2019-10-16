CREATE TABLE IF NOT EXISTS lending_gallery.advisor
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),

    user_id      uuid NOT NULL,
    art_bond_id  uuid NOT NULL,
    position     character varying,

    create_date  TIMESTAMP without time zone,
    update_date  TIMESTAMP without time zone,
    delete_date  TIMESTAMP without time zone,
    object_state character varying,
    CONSTRAINT advisor_pk PRIMARY KEY (id)
);
