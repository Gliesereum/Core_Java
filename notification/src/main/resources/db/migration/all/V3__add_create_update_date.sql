ALTER TABLE notification.user_subscribe ADD COLUMN create_date TIMESTAMP without time zone;
ALTER TABLE notification.user_subscribe ADD COLUMN update_date TIMESTAMP without time zone;

ALTER TABLE notification.user_device ADD COLUMN create_date TIMESTAMP without time zone;
ALTER TABLE notification.user_device ADD COLUMN update_date TIMESTAMP without time zone;