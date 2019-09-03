CREATE TABLE account.user_phone
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    phone character varying,
    user_id uuid,

    CONSTRAINT user_phone_pk PRIMARY KEY (id),
    CONSTRAINT user_phone_user_fk FOREIGN KEY (user_id) REFERENCES account.user (id)
);