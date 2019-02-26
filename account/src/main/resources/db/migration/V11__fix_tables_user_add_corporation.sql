ALTER TABLE account.user drop COLUMN IF EXISTS position;
ALTER TABLE account.corporation drop COLUMN IF EXISTS address;

ALTER TABLE account.corporation ADD COLUMN index character varying;
ALTER TABLE account.corporation ADD COLUMN country character varying;
ALTER TABLE account.corporation ADD COLUMN region character varying;
ALTER TABLE account.corporation ADD COLUMN city character varying;
ALTER TABLE account.corporation ADD COLUMN street character varying;
ALTER TABLE account.corporation ADD COLUMN building_number character varying;
ALTER TABLE account.corporation ADD COLUMN office_number character varying;