CREATE TABLE IF NOT EXISTS karma.agent
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id     uuid,
    active      boolean,
    create_date TIMESTAMP without time zone,
    update_date TIMESTAMP without time zone,
    delete_date TIMESTAMP without time zone,
    object_state character varying,
    CONSTRAINT agent_pk PRIMARY KEY (id)
);