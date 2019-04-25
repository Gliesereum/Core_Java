CREATE TABLE IF NOT EXISTS karma.business_category
(
    id            uuid NOT NULL DEFAULT uuid_generate_v4(),
    code          character varying,
    name          character varying,
    description   character varying,
    image_url      character varying,
    business_type character varying,

    CONSTRAINT business_category_pk PRIMARY KEY (id)
);

ALTER TABLE karma.business DROP COLUMN service_type;
ALTER TABLE karma.business ADD COLUMN business_category_id uuid;

ALTER TABLE karma.record DROP COLUMN service_type;
ALTER TABLE karma.record ADD COLUMN business_category_id uuid;

ALTER TABLE karma.filter DROP COLUMN service_type;
ALTER TABLE karma.filter ADD COLUMN business_category_id uuid;

ALTER TABLE karma.service_class DROP COLUMN service_type;
ALTER TABLE karma.service_class ADD COLUMN business_category_id uuid;

ALTER TABLE karma.service DROP COLUMN service_type;
ALTER TABLE karma.service ADD COLUMN business_category_id uuid;

ALTER TABLE karma.work_space DROP COLUMN service_type;
ALTER TABLE karma.work_space ADD COLUMN business_category_id uuid;

ALTER TABLE karma.work_time DROP COLUMN service_type;
ALTER TABLE karma.work_time ADD COLUMN business_category_id uuid;

