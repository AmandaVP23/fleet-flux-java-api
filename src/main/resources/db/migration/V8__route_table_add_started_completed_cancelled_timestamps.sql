ALTER TABLE route
    ADD started_at timestamp(6) with time zone,
    ADD completed_at timestamp(6) with time zone,
    ADD cancelled_at timestamp(6) with time zone;