CREATE TABLE IF NOT EXISTS notification.user_device
(
    id                          uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id                     uuid,
    firebase_registration_token character varying,
    notification_enable         boolean,
    CONSTRAINT user_device_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification.user_subscribe
(
    id                    uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_device_id        uuid,
    subscribe_destination character varying,
    object_id             uuid,
    notification_enable   boolean,
    CONSTRAINT user_subscribe_pk PRIMARY KEY (id)
);

