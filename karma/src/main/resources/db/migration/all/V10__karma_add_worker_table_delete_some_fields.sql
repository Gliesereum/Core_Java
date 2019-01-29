CREATE TABLE IF NOT EXISTS karma.worker (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    worker_id uuid,
    work_space_id uuid,
    position character varying,
    CONSTRAINT worker_pk PRIMARY KEY (id)
);

ALTER TABLE karma.work_space DROP COLUMN worker_id;

ALTER TABLE karma.work_time RENAME COLUMN business_id TO object_id;

DROP TABLE IF EXISTS karma.service_price_interior, karma.service_price_bodies;