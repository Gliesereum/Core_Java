CREATE TABLE account.depository
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    path character varying,
    owner_id uuid,

    CONSTRAINT depository_pk PRIMARY KEY (id)
);