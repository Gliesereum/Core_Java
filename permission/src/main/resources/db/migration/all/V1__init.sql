CREATE SCHEMA IF NOT EXISTS permission;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pg_catalog;

CREATE TABLE permission.module
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    title character varying,
    description character varying,
    url character varying not null,
    version character varying,
    is_active boolean default true,
    inactive_message character varying,

    CONSTRAINT module_pk PRIMARY KEY (id)
);

CREATE TABLE permission.endpoint
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    title character varying,
    description character varying,
    method character varying not null,
    url character varying not null,
    version character varying,
    is_active boolean default true,
    inactive_message character varying,
    module_id uuid not null,

    CONSTRAINT endpoint_pk PRIMARY KEY (id),
    CONSTRAINT endpoint_module_fk FOREIGN KEY (module_id) REFERENCES permission.module (id)
);

CREATE TABLE permission.group
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  title character varying,
  description character varying,
  purpose character varying not null,
  is_active boolean default true,
  inactive_message character varying,
  parent_group_id uuid,

  CONSTRAINT group_pk PRIMARY KEY (id),
  CONSTRAINT group_parent_group_fk FOREIGN KEY (parent_group_id) REFERENCES permission.group (id)
);

CREATE TABLE permission.group_endpoint
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  group_id uuid not null,
  endpoint_id uuid not null,

  CONSTRAINT group_endpoint_pk PRIMARY KEY (id),
  CONSTRAINT group_endpoint_group_fk FOREIGN KEY (group_id) REFERENCES permission.group (id),
  CONSTRAINT group_endpoint_endpoint_fk FOREIGN KEY (endpoint_id) REFERENCES permission.endpoint (id)
);

CREATE TABLE permission.group_user
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  group_id uuid not null,
  user_id uuid not null,

  CONSTRAINT group_user_pk PRIMARY KEY (id),
  CONSTRAINT group_user_group_fk FOREIGN KEY (group_id) REFERENCES permission.group (id)
);