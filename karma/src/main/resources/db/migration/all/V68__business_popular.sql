CREATE TABLE IF NOT EXISTS karma.business_popular
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    business_id uuid unique,
    count       bigint,
    CONSTRAINT business_popular_pk PRIMARY KEY (id)
);