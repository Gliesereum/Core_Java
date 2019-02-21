ALTER TABLE karma.business ADD COLUMN object_state character varying DEFAULT 'ACTIVE';
ALTER TABLE karma.service_price ADD COLUMN object_state character varying DEFAULT 'ACTIVE';
ALTER TABLE karma.service ADD COLUMN object_state character varying DEFAULT 'ACTIVE';
ALTER TABLE karma.package ADD COLUMN object_state character varying DEFAULT 'ACTIVE';