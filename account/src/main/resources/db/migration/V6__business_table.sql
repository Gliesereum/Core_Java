CREATE TABLE account.business
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    description character varying,
    edrpou character varying,
    kyc_status character varying,
    verified_status character varying,
    address character varying,
    logo_url character varying,
    cover_url character varying,

    CONSTRAINT business_pk PRIMARY KEY (id)
);