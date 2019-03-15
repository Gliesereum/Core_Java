ALTER TABLE lending_gallery.art_bond
  ADD COLUMN stock_count integer,
  ADD COLUMN payment_period integer,
  ADD COLUMN payment_start_date TIMESTAMP without time zone,
  ADD COLUMN payment_finish_date TIMESTAMP without time zone;

ALTER TABLE lending_gallery.operation_story
  ADD COLUMN stock_count integer,
  ALTER COLUMN sum TYPE double precision;




