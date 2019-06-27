CREATE TABLE IF NOT EXISTS karma.working_space_price(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    price_id uuid,
    working_space_id uuid,
    CONSTRAINT working_space_price_pk PRIMARY KEY (id)
);

