CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account.user
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    username character varying,
    firstname character varying,
    lastname character varying,
    gender character varying,

    CONSTRAINT user_pk PRIMARY KEY (id)
);