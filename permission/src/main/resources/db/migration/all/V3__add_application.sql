CREATE TABLE permission.application
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying,
    description character varying,
    is_active boolean default true,

    CONSTRAINT application_pk PRIMARY KEY (id)
);

CREATE TABLE permission.application_host
(
    application_id uuid not null,
    host character varying not null,
    CONSTRAINT application_host_application_id_fk FOREIGN KEY (application_id) REFERENCES permission.application (id)
);

ALTER TABLE permission.group
ADD COLUMN application_id uuid;