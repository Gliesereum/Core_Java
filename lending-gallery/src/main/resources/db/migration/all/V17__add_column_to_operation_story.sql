ALTER TABLE lending_gallery.operation_story
    ADD COLUMN document_url character varying,
    ADD COLUMN comment character varying,
    ADD COLUMN operation_status character varying DEFAULT 'SEND';
