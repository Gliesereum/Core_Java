CREATE TABLE media.user_file
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    filename character varying,
    url character varying,
    media_type character varying not null,
    size bigint,
    user_id uuid,

    CONSTRAINT user_file_pk PRIMARY KEY (id)
);