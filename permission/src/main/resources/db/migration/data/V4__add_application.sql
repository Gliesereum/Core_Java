INSERT INTO permission.application (id, name, description, is_active) VALUES ('041a8a6e-6873-49af-9614-1dc9826a4c01', 'Coupler Client', 'Coupler client application', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('80f43f03-a191-4f08-b466-89967e705816', 'Coupler Worker', 'Coupler worker application', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('d913ada7-a22f-44dd-a8af-ad612d0b83c3', 'Coupler Business', 'Coupler business application', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('4ba91ec3-5a50-400f-9d29-08e4f6f022e9', 'Coupler Client Web Widget', 'Coupler client web widget', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('b25f27f0-fce8-4565-889e-496fbdaed803', 'Quark', 'Quark', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('a4c3f66f-ae6c-4faf-ad80-ad13236d4b63', 'Lending gallery', 'Lending gallery', true)
ON CONFLICT (id) DO NOTHING;
INSERT INTO permission.application (id, name, description, is_active) VALUES ('9e253241-1e80-4fd0-84c4-4814f0c35a65', 'Coupler Dashboard', 'Coupler Dashboard', true)
ON CONFLICT (id) DO NOTHING;