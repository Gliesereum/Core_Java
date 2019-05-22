CREATE TABLE IF NOT EXISTS karma.chat (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id uuid,
    business_id uuid,
    CONSTRAINT chat_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.chat_support (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id uuid,
    business_id uuid,
    CONSTRAINT chat_support_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.chat_message (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    chat_id uuid,
    message character varying,
    create_date TIMESTAMP without time zone,
    CONSTRAINT chat_message_pk PRIMARY KEY (id)
);
