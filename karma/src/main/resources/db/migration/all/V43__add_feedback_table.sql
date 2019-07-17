CREATE TABLE IF NOT EXISTS karma.feedback (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    object_id uuid,
    client_id uuid,
    business_id uuid,
    working_space_id uuid,
    worker_id uuid,
    comment character varying,
    mark integer,
    date_create_object timestamp without time zone,
    date_feedback timestamp without time zone,
    CONSTRAINT feedback_pk PRIMARY KEY (id)
);
