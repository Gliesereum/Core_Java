INSERT INTO account.user (id, first_name, last_name, middle_name, gender, ban_status, country, city, address, add_address, avatar_url, cover_url, kyc_approved, last_sign_in, last_activity, create_date, update_date, delete_date, object_state)
VALUES ('3d1eafa8-16de-490b-b6f5-5d3c1f2cd48f', 'Иван', 'Иванов ', 'Иванович', 'UNKNOWN', 'UNBAN', null, null, null, null, 'https://glcdn.sgp1.digitaloceanspaces.com/coupler/Avatar.png', 'https://glcdn.sgp1.digitaloceanspaces.com/coupler/Cover.png', false, '2019-07-29 12:01:44.541354', null, '2019-07-29 12:01:44.541354', '2019-07-29 12:01:44.541354', null, 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO account.user_phone (id, phone, user_id)
VALUES ('84fb522f-ea8a-41dd-b00d-afeb8295a649', '380888080808', '3d1eafa8-16de-490b-b6f5-5d3c1f2cd48f')
ON CONFLICT (id) DO NOTHING;
