
ALTER TABLE organization
    ADD COLUMN slug VARCHAR(255);

UPDATE organization
    SET slug = realm;

ALTER TABLE organization
    ALTER COLUMN slug SET NOT NULL;

ALTER TABLE organization
    ADD CONSTRAINT organization_slug_unique UNIQUE (slug);