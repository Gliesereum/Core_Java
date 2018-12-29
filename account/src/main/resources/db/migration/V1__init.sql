CREATE SCHEMA IF NOT EXISTS account;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pg_catalog;

CREATE TABLE account.user
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    first_name character varying,
    last_name character varying,
    middle_name character varying,
    gender character varying,
    ban_status character varying,
    position character varying,
    country character varying,
    city character varying,
    address character varying,
    add_address character varying,
    avatar_url character varying,
    cover_url character varying,

    CONSTRAINT user_pk PRIMARY KEY (id)
);