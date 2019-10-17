CREATE TABLE IF NOT EXISTS lending_gallery.offer_comment
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    create_by_id uuid NOT NULL,
    offer_id     uuid NOT NULL,
    comment      character varying,
    state_type   character varying,
    create_date  TIMESTAMP without time zone,

    CONSTRAINT offer_comment_pk PRIMARY KEY (id)
);
