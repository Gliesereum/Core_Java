CREATE TABLE IF NOT EXISTS karma.bonus_score
(
    id          uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id     uuid,
    score       double precision,
    create_date TIMESTAMP without time zone,
    update_date TIMESTAMP without time zone,
    CONSTRAINT bonus_score_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS karma.bonus_score_history
(
    id             uuid NOT NULL DEFAULT uuid_generate_v4(),
    bonus_score_id uuid,
    value          double precision,
    create_date    TIMESTAMP without time zone,
    update_date    TIMESTAMP without time zone,
    CONSTRAINT bonus_score_history_pk PRIMARY KEY (id)
);