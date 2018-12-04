CREATE TABLE account.user_business
(

    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id uuid,
    name character varying,
    description character varying,
    edrpou character varying,
    kyc_status character varying,
    address character varying,
    logo_url character varying,
    cover_url character varying,

    CONSTRAINT user_user_business_pk PRIMARY KEY (id),
    CONSTRAINT user_user_business_user_fk FOREIGN KEY (user_id) REFERENCES account.user (id)
);