CREATE TABLE file.user_file
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    filename character varying,
    url character varying,
    media_type character varying not null,
    size bigint,
    user_id uuid,
    open boolean,
    crypto boolean,

    CONSTRAINT user_file_pk PRIMARY KEY (id)
);

CREATE TABLE file.user_file_key
(
    user_file_id uuid not null,
    key character varying not null,
    CONSTRAINT user_file_key_user_file_id_fk FOREIGN KEY (user_file_id) REFERENCES file.user_file (id)
);

CREATE TABLE file.user_file_reader
(
    user_file_id uuid not null,
    reader_id uuid not null,
    CONSTRAINT user_file_reader_user_file_id_fk FOREIGN KEY (user_file_id) REFERENCES file.user_file (id)
);
