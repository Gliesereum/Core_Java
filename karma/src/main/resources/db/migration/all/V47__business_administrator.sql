CREATE TABLE IF NOT EXISTS karma.business_administrator
(
    id                 uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id          uuid,
    business_id          uuid,
    corporation_id        uuid,
    CONSTRAINT business_administrator_pk PRIMARY KEY (id)
);
