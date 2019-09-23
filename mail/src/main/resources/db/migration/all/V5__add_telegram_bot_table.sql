CREATE TABLE IF NOT EXISTS mail.telegram_bot (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   chat_id bigint,
   chat_name character varying,
   active boolean,
   CONSTRAINT telegram_bot_pk PRIMARY KEY (id)
);
