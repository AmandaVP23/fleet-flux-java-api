ALTER TABLE route
    ADD COLUMN distance_meters double precision,
    ADD COLUMN duration_seconds double precision;

UPDATE route r
SET distance_meters = d.distance_meters,
    duration_seconds = d.duration_seconds
FROM route_details d
WHERE d.route_id = r.id;

ALTER TABLE route
    ALTER COLUMN distance_meters SET NOT NULL,
    ALTER COLUMN duration_seconds SET NOT NULL;

ALTER TABLE route_details
    DROP COLUMN distance_meters,
    DROP COLUMN duration_seconds;