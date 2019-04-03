CREATE TABLE IF NOT EXISTS mail.mail_state (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   phone character varying,
   text character varying,
   message_id character varying,
   http_status character varying,
   message_status integer,
   create_date TIMESTAMP without time zone DEFAULT NOW(),
   CONSTRAINT mail_state_pk PRIMARY KEY (id)
);
