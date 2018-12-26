ALTER TABLE karma.package_service DROP CONSTRAINT package_service_service_fk;

ALTER TABLE karma.package_service ADD CONSTRAINT package_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service_price (id)