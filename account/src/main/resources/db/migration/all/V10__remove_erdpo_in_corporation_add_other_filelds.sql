ALTER TABLE account.corporation
  drop COLUMN IF EXISTS edrpou;


ALTER TABLE account.corporation ADD COLUMN company_type character varying;
ALTER TABLE account.corporation ADD COLUMN business_activity character varying;
ALTER TABLE account.corporation ADD COLUMN rc_number character varying;
ALTER TABLE account.corporation ADD COLUMN place_incorporation character varying;
ALTER TABLE account.corporation ADD COLUMN date_incorporation TIMESTAMP without time zone;