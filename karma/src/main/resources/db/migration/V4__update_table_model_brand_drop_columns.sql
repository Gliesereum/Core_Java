UPDATE karma.model_car
SET brand_id = T.id
FROM (select bc.id, mc.brand_id_int
FROM karma.model_car mc
join karma.brand_car bc on bc.id_car = mc.brand_id_int
WHERE mc.brand_id_int = bc.id_car)T
WHERE T.brand_id_int = model_car.brand_id_int;

ALTER TABLE karma.brand_car  DROP COLUMN IF EXISTS id_car;
ALTER TABLE karma.model_car  DROP COLUMN IF EXISTS brand_id_int, DROP COLUMN IF EXISTS id_model;