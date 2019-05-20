CREATE TABLE IF NOT EXISTS karma.client_preference
(
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  client_id uuid NOT NULL,
  service_id  uuid NOT NULL,
  business_category_id  uuid NOT NULL,

  CONSTRAINT client_preference_pk PRIMARY KEY (id)
);

CREATE INDEX inx_client_preference_client_id_and_service_id ON karma.client_preference (client_id, service_id);