CREATE TABLE account.referral_code
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id     uuid NOT NULL,
    code        character varying,
    create_date TIMESTAMP without time zone,

    CONSTRAINT referral_code_pk PRIMARY KEY (id)
);