CREATE TABLE account.user_business (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  user_id uuid not null,
  business_id uuid not null,

  CONSTRAINT user_business_pk PRIMARY KEY (id),
  CONSTRAINT user_business_user_fk FOREIGN KEY (user_id) REFERENCES account.user (id),
  CONSTRAINT user_business_business_fk FOREIGN KEY (business_id) REFERENCES account.business (id)
);