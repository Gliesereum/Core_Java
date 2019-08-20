CREATE TABLE IF NOT EXISTS payment.payment_recipient (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   public_key character varying,
   private_key character varying,
   object_id uuid,
   CONSTRAINT payment_recipient_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.payment_order (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   CONSTRAINT payment_order_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.way_for_pay_card (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   rec_token character varying,
   client_name character varying,
   phone character varying,
   email character varying,
   card_mask character varying,
   card_type character varying,
   issuer_bank_country character varying,
   issuer_bank_name character varying,
   create_date timestamp without time zone,
   processing_date timestamp without time zone,
   owner_id uuid,
   is_favorite boolean,
   is_verify boolean,
   reason_code integer,
   reason character varying,
   CONSTRAINT way_for_pay_card_pk PRIMARY KEY (id)
);
