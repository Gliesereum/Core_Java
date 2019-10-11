CREATE TABLE IF NOT EXISTS notification.telegram_chat
(
    id      uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id uuid,
    chat_id bigint,
    CONSTRAINT telegram_chat_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification.telegram_chat_action
(
    id      uuid NOT NULL DEFAULT uuid_generate_v4(),
    action  character varying,
    chat_id bigint,
    value   character varying,
    CONSTRAINT telegram_chat_action_pk PRIMARY KEY (id)
);
