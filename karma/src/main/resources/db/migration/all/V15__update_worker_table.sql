ALTER TABLE karma.worker ADD COLUMN user_id uuid;
ALTER TABLE karma.worker ADD COLUMN business_id uuid;

ALTER TABLE karma.worker DROP COLUMN worker_id;
