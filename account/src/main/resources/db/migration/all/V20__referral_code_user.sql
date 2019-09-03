CREATE TABLE account.referral_code_user
(
    id               uuid NOT NULL DEFAULT uuid_generate_v4(),
    user_id          uuid NOT NULL,
    referrer_id      uuid,
    referral_code_id uuid,

    CONSTRAINT referral_code_user_pk PRIMARY KEY (id)
);