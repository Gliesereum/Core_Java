alter table karma.business
    alter column business_verify set default true;

UPDATE karma.business
SET business_verify = true
WHERE business_verify IS NULL;