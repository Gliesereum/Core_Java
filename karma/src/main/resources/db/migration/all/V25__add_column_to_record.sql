ALTER TABLE karma.record ADD COLUMN notification_send boolean default false;

UPDATE karma.record
SET notification_send = false;