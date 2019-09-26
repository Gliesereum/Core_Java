CREATE TABLE IF NOT EXISTS payment.payment_recipient
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    public_key  character varying,
    private_key character varying,
    object_id   uuid,
    CONSTRAINT payment_recipient_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.wfp_order
(
    id                  uuid NOT NULL DEFAULT uuid_generate_v4(),
    order_reference     uuid,
    client_id           uuid,
    card_pan            character varying,
    card_type           character varying,
    issuer_bank_country character varying,
    issuer_bank_name    character varying,
    rec_token           character varying,
    payment_system      character varying,
    create_date         timestamp without time zone,
    processing_date     timestamp without time zone,
    CONSTRAINT wfp_order_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.wfp_order_transaction
(
    id                    uuid NOT NULL DEFAULT uuid_generate_v4(),
    order_id              uuid,
    reason_code           integer,
    fee                   float8,
    amount                float8,
    currency              character varying,
    auth_code             character varying,
    transactions_status   character varying,
    transactions_type     character varying,
    pay_transactions_type character varying,
    reason                character varying,
    transaction_date      timestamp without time zone,

    CONSTRAINT wfp_order_transaction_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS payment.wfp_card
(
    id                  uuid NOT NULL DEFAULT uuid_generate_v4(),
    rec_token           character varying,
    client_name         character varying,
    phone               character varying,
    email               character varying,
    card_mask           character varying,
    card_type           character varying,
    issuer_bank_country character varying,
    issuer_bank_name    character varying,
    create_date         timestamp without time zone,
    processing_date     timestamp without time zone,
    owner_id            uuid,
    is_favorite         boolean,
    is_verify           boolean,
    reason_code         integer,
    reason              character varying,
    CONSTRAINT wfp_card_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.wfp_response_code
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    reason_code integer,
    name        character varying,
    reason      character varying,
    CONSTRAINT wfp_response_code_pk PRIMARY KEY (id)
);
