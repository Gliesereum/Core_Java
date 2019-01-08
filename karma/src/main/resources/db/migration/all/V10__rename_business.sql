ALTER TABLE karma.car_wash
  RENAME COLUMN business_id to corporation_id;

ALTER TABLE karma.work_time
  RENAME COLUMN business_service_id to corporation_service_id;

ALTER TABLE karma.work_space
  RENAME COLUMN business_service_id to corporation_service_id;

ALTER TABLE karma.service_price
  RENAME COLUMN business_service_id to corporation_service_id;

ALTER TABLE karma.package
 RENAME COLUMN business_service_id to corporation_service_id;