CREATE TABLE IF NOT EXISTS karma.client
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id      uuid,
    first_name   character varying,
    last_name    character varying,
    middle_name  character varying,
    phone        character varying,
    email        character varying,
    avatar_url   character varying,
    create_date  TIMESTAMP without time zone,
    update_date  TIMESTAMP without time zone,
    delete_date  TIMESTAMP without time zone,
    object_state character varying,
    CONSTRAINT client_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.client_business
(
    client_id   uuid NOT NULL,
    business_id uuid NOT NULL,
    CONSTRAINT client_business_pk PRIMARY KEY (client_id, business_id)
);

CREATE TABLE IF NOT EXISTS karma.client_corporation
(
    client_id      uuid NOT NULL,
    corporation_id uuid NOT NULL,
    CONSTRAINT client_corporation_pk PRIMARY KEY (client_id, corporation_id)
);