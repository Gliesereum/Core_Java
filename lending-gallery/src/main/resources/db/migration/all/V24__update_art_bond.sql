ALTER TABLE lending_gallery.art_bond
    ADD COLUMN object_state character varying,
    ADD COLUMN create_date  TIMESTAMP without time zone,
    ADD COLUMN update_date  TIMESTAMP without time zone,
    ADD COLUMN delete_date  TIMESTAMP without time zone;
