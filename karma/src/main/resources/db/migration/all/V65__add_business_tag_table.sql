CREATE TABLE IF NOT EXISTS karma.business_tag
(
    id uuid  NOT NULL DEFAULT uuid_generate_v4(),
    tag_id  uuid  NOT NULL,
    business_id  uuid  NOT NULL,
    CONSTRAINT business_tag_pk PRIMARY KEY (id)
);
