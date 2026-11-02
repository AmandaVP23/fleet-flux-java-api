ALTER TABLE tracking_device
    DROP COLUMN serial_number;

ALTER TABLE tracking_device
    ADD imei VARCHAR(15);