CREATE TABLE IF NOT EXISTS karma.comment
(
  id        uuid NOT NULL DEFAULT uuid_generate_v4(),
  object_id uuid NOT NULL,
  text      character varying,
  rating    integer,
  owner_id  uuid,

  CONSTRAINT comment_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.karma
(
  id            uuid    NOT NULL DEFAULT uuid_generate_v4(),
  object_id     uuid    NOT NULL,
  current_value bigint  NOT NULL,
  fix_value     bigint  NOT NULL,
  level         integer NOT NULL,

  CONSTRAINT karma_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.media
(
  id          uuid NOT NULL DEFAULT uuid_generate_v4(),
  object_id   uuid NOT NULL,
  title       character varying,
  description character varying,
  url         character varying,
  media_type  character varying,

  CONSTRAINT media_pk PRIMARY KEY (id)
);