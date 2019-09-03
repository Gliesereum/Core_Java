alter table account.business
  rename constraint business_pk to corporation_pk;

alter table account.business
  rename to corporation;

alter table account.user_business
  rename column business_id to corporation_id;

alter table account.user_business
  rename constraint user_business_pk to user_corporation_pk;

alter table account.user_business
  rename constraint user_business_user_fk to user_corporation_user_fk;

alter table account.user_business
  rename constraint user_business_business_fk to user_corporation_corporation_fk;

alter table account.user_business
  rename to user_corporation;

