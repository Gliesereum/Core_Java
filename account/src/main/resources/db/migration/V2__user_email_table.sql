CREATE TABLE account.user_email
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    email character varying,
    user_id uuid,

    CONSTRAINT user_email_pk PRIMARY KEY (id),
    CONSTRAINT user_email_user_fk FOREIGN KEY (user_id) REFERENCES account.user (id)
);