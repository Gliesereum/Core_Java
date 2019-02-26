CREATE TABLE account.corporation_employee
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    first_name character varying,
    last_name character varying,
    position character varying,
    corporation_id uuid,

    CONSTRAINT corporation_employee_pk PRIMARY KEY (id),
    CONSTRAINT corporation_employee_corporation_fk FOREIGN KEY (corporation_id) REFERENCES account.corporation (id)
);