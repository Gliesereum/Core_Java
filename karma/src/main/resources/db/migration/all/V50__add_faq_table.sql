CREATE TABLE IF NOT EXISTS karma.faq (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    title character varying,
    description character varying,
    CONSTRAINT faq_pk PRIMARY KEY (id)
);
