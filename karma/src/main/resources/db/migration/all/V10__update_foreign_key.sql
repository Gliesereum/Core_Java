ALTER TABLE karma.car_wash_record_service DROP CONSTRAINT car_wash_record_service_service_fk;

ALTER TABLE karma.car_wash_record_service ADD CONSTRAINT car_wash_record_service_service_fk FOREIGN KEY (service_id) REFERENCES karma.service_price (id)