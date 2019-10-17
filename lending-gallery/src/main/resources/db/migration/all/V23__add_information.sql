CREATE TABLE IF NOT EXISTS lending_gallery.information
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    title       character varying,
    description character varying,
    video_url   character varying,
    image_url   character varying,
    tag         character varying,
    iso_code    character varying,
    index       integer,
    CONSTRAINT information_pk PRIMARY KEY (id)
);
