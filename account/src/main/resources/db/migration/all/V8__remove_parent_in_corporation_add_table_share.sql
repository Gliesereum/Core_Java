ALTER TABLE account.corporation
  drop COLUMN IF EXISTS parent_corporation_id;


CREATE TABLE IF NOT EXISTS account.corporation_shared_ownership (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    corporation_id uuid,
    owner_id uuid,
    owner_corporation_id uuid,
    share int,
    CONSTRAINT corporation_shared_ownership_pk PRIMARY KEY (id)
);

