CREATE TABLE IF NOT EXISTS mail.feed_back_user (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   user_id uuid,
   app_id uuid,
   CONSTRAINT feed_back_user_pk PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS mail.feed_back_request (
   id uuid NOT NULL DEFAULT uuid_generate_v4(),
   phone character varying,
   request_processed boolean,
   app_id uuid,
   create_date TIMESTAMP without time zone DEFAULT NOW(),
   CONSTRAINT feed_back_request_pk PRIMARY KEY (id)
);