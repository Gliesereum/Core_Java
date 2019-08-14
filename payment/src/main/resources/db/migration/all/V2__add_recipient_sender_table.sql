CREATE TABLE IF NOT EXISTS payment.payment_recipient (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   public_key character varying,
   private_key character varying,
   object_id uuid,
   CONSTRAINT payment_recipient_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.payment_sender (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   card_token character varying,
   object_id uuid,
   is_favorite boolean,
   CONSTRAINT payment_sender_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS payment.payment_order (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   CONSTRAINT payment_order_pk PRIMARY KEY (id)
);
